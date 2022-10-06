package org.smartregister.chw.agyw.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.constants.Gender;
import org.smartregister.agyw.R;
import org.smartregister.chw.agyw.AGYWLibrary;
import org.smartregister.chw.agyw.contract.BaseAGYWCallDialogContract;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.helper.ECSyncHelper;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.PermissionUtils;
import org.smartregister.util.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import timber.log.Timber;

import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.Utils.getAllSharedPreferences;

public class AGYWUtil {
    protected static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    protected static DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());

    public static void processEvent(AllSharedPreferences allSharedPreferences, Event baseEvent) throws Exception {
        if (baseEvent != null) {
            AGYWJsonFormUtils.tagEvent(allSharedPreferences, baseEvent);
            JSONObject eventJson = new JSONObject(AGYWJsonFormUtils.gson.toJson(baseEvent));

            getSyncHelper().addEvent(baseEvent.getBaseEntityId(), eventJson, BaseRepository.TYPE_Unprocessed);
            startClientProcessing();
        }
    }

    public static void startClientProcessing() {
        try {
            long lastSyncTimeStamp = getAllSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            getClientProcessorForJava().processClient(getSyncHelper().getEvents(lastSyncDate, BaseRepository.TYPE_Unprocessed));
            getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());
        } catch (Exception e) {
            Timber.d(e);
        }
    }

    public static ECSyncHelper getSyncHelper() {
        return AGYWLibrary.getInstance().getEcSyncHelper();
    }

    public static ClientProcessorForJava getClientProcessorForJava() {
        return AGYWLibrary.getInstance().getClientProcessorForJava();
    }

    public static Spanned fromHtml(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    @SuppressLint("HardwareIds")
    public static boolean launchDialer(final Activity activity, final BaseAGYWCallDialogContract.View callView, final String phoneNumber) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            // set a pending call execution request
            if (callView != null) {
                callView.setPendingCallRequest(() -> AGYWUtil.launchDialer(activity, callView, phoneNumber));
            }

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, PermissionUtils.PHONE_STATE_PERMISSION_REQUEST_CODE);

            return false;
        } else {

            if (((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number()
                    == null) {

                Timber.i("No dial application so we launch copy to clipboard...");

                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(activity.getText(R.string.copied_phone_number), phoneNumber);
                clipboard.setPrimaryClip(clip);

                CopyToClipboardDialog copyToClipboardDialog = new CopyToClipboardDialog(activity, R.style.copy_clipboard_dialog);
                copyToClipboardDialog.setContent(phoneNumber);
                copyToClipboardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                copyToClipboardDialog.show();
                // no phone
                Toast.makeText(activity, activity.getText(R.string.copied_phone_number), Toast.LENGTH_SHORT).show();

            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                activity.startActivity(intent);
            }
            return true;
        }
    }

    public static void saveFormEvent(final String jsonString) throws Exception {
        AllSharedPreferences allSharedPreferences = AGYWLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = AGYWJsonFormUtils.processJsonForm(allSharedPreferences, jsonString);
        //add guard clause to break processing if client did not consent
        if(baseEvent!= null && baseEvent.getEventType().equalsIgnoreCase(Constants.EVENT_TYPE.AGYW_REGISTRATION) && !isClientToBeRegistered(jsonString)){
            return;
        }
        if (baseEvent!= null &&  baseEvent.getEventType().equalsIgnoreCase(Constants.EVENT_TYPE.AGYW_REGISTRATION)) {
            baseEvent.addObs(new Obs().withFormSubmissionField(Constants.JSON_FORM_KEY.UIC_ID).withValue(generateUICID(baseEvent.getBaseEntityId(), jsonString))
                    .withFieldCode(Constants.JSON_FORM_KEY.UIC_ID).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<>()));
        }
        AGYWUtil.processEvent(allSharedPreferences, baseEvent);
    }
    public static String generateUICID(String baseEntityId, String jsonString) throws ParseException {

        CommonPersonObjectClient client = getCommonPersonObjectClient(baseEntityId);

        String firstName = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.FIRST_NAME, false);
        String lastName = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.LAST_NAME, false);
        String dob = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.DOB, false);

        Date inputDob = inputFormat.parse(dob);

        String dobString = df.format(inputDob);
        String birthLocation = getClientBirthRegionFromForm(jsonString);

        String UIC_ID = "";


        //UIC ID is formed by
        // the last two letters from the first and last name of the client
        UIC_ID += firstName.length() > 2 ? firstName.substring(firstName.length() - 2) : firstName;
        UIC_ID += lastName.length() > 2 ? lastName.substring(lastName.length() - 2) : lastName;
        // first three letters from the client's region of birth
        UIC_ID += birthLocation.length() > 3 ? birthLocation.substring(0, 3) : birthLocation;
        //if the client is male 1 else 2, for AGYW the client is always female
        UIC_ID += 2;
        //first two digits of the clients birth date
        //last two digits of the client's birth year
        UIC_ID += dobString.length() > 2 ? dobString.substring(0, 2) : dobString;
        UIC_ID += dobString.length() > 2 ? dobString.substring(dobString.length() - 2) : dobString;

        if (StringUtils.isNotBlank(UIC_ID))
            return UIC_ID;
        return "";
    }

    private static String getClientBirthRegionFromForm(String jsonString) {
        JSONObject form;
        try {
            form = new JSONObject(jsonString);
            JSONArray fields = AGYWJsonFormUtils.agywFormFields(form);
            JSONObject birth_region = AGYWJsonFormUtils.getFieldJSONObject(fields, "birth_region");
            if (birth_region != null) {
                String birthRegVal = birth_region.getString(VALUE);
                if (StringUtils.isNotBlank(birthRegVal))
                    return birthRegVal;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isClientToBeRegistered(String jsonString) {
        JSONObject form;
        try {
            form = new JSONObject(jsonString);
            JSONArray fields = AGYWJsonFormUtils.agywFormFields(form);
            JSONObject consent = AGYWJsonFormUtils.getFieldJSONObject(fields, "consent_to_be_enrolled");
            if (consent != null) {
                String consentVal = consent.getString(VALUE);
                if (StringUtils.isNotBlank(consentVal) && consentVal.equalsIgnoreCase("yes"))
                    return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static CommonPersonObjectClient getCommonPersonObjectClient(@NonNull String baseEntityId) {
        CommonRepository commonRepository = AGYWLibrary.getInstance().context().commonrepository(Constants.TABLES.FAMILY_MEMBER_TABLE);

        final CommonPersonObject commonPersonObject = commonRepository.findByBaseEntityId(baseEntityId);
        final CommonPersonObjectClient client =
                new CommonPersonObjectClient(commonPersonObject.getCaseId(), commonPersonObject.getDetails(), "");
        client.setColumnmaps(commonPersonObject.getColumnmaps());
        return client;
    }

    public static int getMemberProfileImageResourceIdentifier(String entityType) {
        return R.mipmap.ic_member;
    }

    public static String getGenderTranslated(Context context, String gender) {
        if (gender.equalsIgnoreCase(Gender.MALE.toString())) {
            return context.getResources().getString(R.string.male);
        } else if (gender.equalsIgnoreCase(Gender.FEMALE.toString())) {
            return context.getResources().getString(R.string.female);
        }
        return "";
    }

    public static void crateGraduateFromServicesEvent(String baseEntityId) throws Exception {
        AllSharedPreferences allSharedPreferences = AGYWLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = getBaseEvent(allSharedPreferences, Constants.EVENT_TYPE.AGYW_GRADUATE_SERVICES, baseEntityId);
        AGYWUtil.processEvent(allSharedPreferences, baseEvent);
    }

    private static Event getBaseEvent(AllSharedPreferences allSharedPreferences, String eventName, String baseEntityId) {
        return (Event) new Event()
                .withBaseEntityId(baseEntityId)
                .withEventDate(new Date())
                .withFormSubmissionId(JsonFormUtils.generateRandomUUIDString())
                .withEventType(eventName)
                .withEntityType(Constants.TABLES.AGYW_FOLLOW_UP)
                .withProviderId(allSharedPreferences.fetchRegisteredANM())
                .withLocationId(allSharedPreferences.fetchDefaultLocalityId(allSharedPreferences.fetchRegisteredANM()))
                .withTeamId(allSharedPreferences.fetchDefaultTeamId(allSharedPreferences.fetchRegisteredANM()))
                .withTeam(allSharedPreferences.fetchDefaultTeam(allSharedPreferences.fetchRegisteredANM()))
                .withClientDatabaseVersion(AGYWLibrary.getInstance().getDatabaseVersion())
                .withClientApplicationVersion(AGYWLibrary.getInstance().getApplicationVersion())
                .withDateCreated(new Date());
    }
}
