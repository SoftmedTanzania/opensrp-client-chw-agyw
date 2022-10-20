package org.smartregister.chw.agyw.util;

import android.os.Build;
import android.util.Log;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.agyw.AGYWLibrary;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.FormUtils;

import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;

import static org.smartregister.chw.agyw.util.Constants.ENCOUNTER_TYPE;
import static org.smartregister.chw.agyw.util.Constants.STEP_EIGHT;
import static org.smartregister.chw.agyw.util.Constants.STEP_FIVE;
import static org.smartregister.chw.agyw.util.Constants.STEP_FOUR;
import static org.smartregister.chw.agyw.util.Constants.STEP_NINE;
import static org.smartregister.chw.agyw.util.Constants.STEP_ONE;
import static org.smartregister.chw.agyw.util.Constants.STEP_SEVEN;
import static org.smartregister.chw.agyw.util.Constants.STEP_SIX;
import static org.smartregister.chw.agyw.util.Constants.STEP_TEN;
import static org.smartregister.chw.agyw.util.Constants.STEP_THREE;
import static org.smartregister.chw.agyw.util.Constants.STEP_TWO;

public class AGYWJsonFormUtils extends org.smartregister.util.JsonFormUtils {
    public static final String METADATA = "metadata";

    public static Triple<Boolean, JSONObject, JSONArray> validateParameters(String jsonString) {

        JSONObject jsonForm = toJSONObject(jsonString);
        JSONArray fields = agywFormFields(jsonForm);

        Triple<Boolean, JSONObject, JSONArray> registrationFormParams = Triple.of(jsonForm != null && fields != null, jsonForm, fields);
        return registrationFormParams;
    }

    public static JSONArray agywFormFields(JSONObject jsonForm) {
        //TODO: refactor this implementation with a O(logN) complexity
        try {
            JSONArray fields = new JSONArray();
            JSONArray fieldsOne = fields(jsonForm, STEP_ONE);
            JSONArray fieldsTwo = fields(jsonForm, STEP_TWO);
            JSONArray fieldsThree = fields(jsonForm, STEP_THREE);
            JSONArray fieldsFour = fields(jsonForm, STEP_FOUR);
            JSONArray fieldsFive = fields(jsonForm, STEP_FIVE);
            JSONArray fieldsSix = fields(jsonForm, STEP_SIX);
            JSONArray fieldsSeven = fields(jsonForm, STEP_SEVEN);
            JSONArray fieldsEight = fields(jsonForm, STEP_EIGHT);
            JSONArray fieldsNine = fields(jsonForm, STEP_NINE);
            JSONArray fieldsTen = fields(jsonForm, STEP_TEN);

            compileFields(fields, fieldsOne);
            compileFields(fields, fieldsTwo);
            compileFields(fields, fieldsThree);
            compileFields(fields, fieldsFour);
            compileFields(fields, fieldsFive);
            compileFields(fields, fieldsSix);
            compileFields(fields, fieldsSeven);
            compileFields(fields, fieldsEight);
            compileFields(fields, fieldsNine);
            compileFields(fields, fieldsTen);

            return fields;

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    private static void compileFields(JSONArray compiledFields, JSONArray addedField) throws JSONException {
        if (addedField != null) {
            for (int i = 0; i < addedField.length(); i++) {
                compiledFields.put(addedField.get(i));
            }
        }
    }

    public static JSONArray fields(JSONObject jsonForm, String step) {
        try {

            JSONObject step1 = jsonForm.has(step) ? jsonForm.getJSONObject(step) : null;
            if (step1 == null) {
                return null;
            }

            return step1.has(FIELDS) ? step1.getJSONArray(FIELDS) : null;

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    public static Event processJsonForm(AllSharedPreferences allSharedPreferences, String
            jsonString) {

        Triple<Boolean, JSONObject, JSONArray> registrationFormParams = validateParameters(jsonString);

        if (!registrationFormParams.getLeft()) {
            return null;
        }

        JSONObject jsonForm = registrationFormParams.getMiddle();
        JSONArray fields = registrationFormParams.getRight();
        String entityId = getString(jsonForm, ENTITY_ID);
        String encounter_type = jsonForm.optString(Constants.JSON_FORM_EXTRA.ENCOUNTER_TYPE);

        if (Constants.EVENT_TYPE.AGYW_REGISTRATION.equals(encounter_type)) {
            encounter_type = Constants.TABLES.AGYW_REGISTER;
        } else if (Constants.EVENT_TYPE.AGYW_FOLLOW_UP_VISIT.equals(encounter_type)) {
            encounter_type = Constants.TABLES.AGYW_FOLLOW_UP;
        }
        return org.smartregister.util.JsonFormUtils.createEvent(fields, getJSONObject(jsonForm, METADATA), formTag(allSharedPreferences), entityId, getString(jsonForm, ENCOUNTER_TYPE), encounter_type);
    }

    protected static FormTag formTag(AllSharedPreferences allSharedPreferences) {
        FormTag formTag = new FormTag();
        formTag.providerId = allSharedPreferences.fetchRegisteredANM();
        formTag.appVersion = AGYWLibrary.getInstance().getApplicationVersion();
        formTag.databaseVersion = AGYWLibrary.getInstance().getDatabaseVersion();
        return formTag;
    }

    public static void tagEvent(AllSharedPreferences allSharedPreferences, Event event) {
        String providerId = allSharedPreferences.fetchRegisteredANM();
        event.setProviderId(providerId);
        event.setLocationId(locationId(allSharedPreferences));
        event.setChildLocationId(allSharedPreferences.fetchCurrentLocality());
        event.setTeam(allSharedPreferences.fetchDefaultTeam(providerId));
        event.setTeamId(allSharedPreferences.fetchDefaultTeamId(providerId));

        event.setClientApplicationVersion(AGYWLibrary.getInstance().getApplicationVersion());
        event.setClientDatabaseVersion(AGYWLibrary.getInstance().getDatabaseVersion());
    }

    private static String locationId(AllSharedPreferences allSharedPreferences) {
        String providerId = allSharedPreferences.fetchRegisteredANM();
        String userLocationId = allSharedPreferences.fetchUserLocalityId(providerId);
        if (StringUtils.isBlank(userLocationId)) {
            userLocationId = allSharedPreferences.fetchDefaultLocalityId(providerId);
        }

        return userLocationId;
    }

    public static void getRegistrationForm(JSONObject jsonObject, String entityId, String
            currentLocationId) throws JSONException {
        jsonObject.getJSONObject(METADATA).put(ENCOUNTER_LOCATION, currentLocationId);
        jsonObject.put(org.smartregister.util.JsonFormUtils.ENTITY_ID, entityId);
        jsonObject.put(DBConstants.KEY.RELATIONAL_ID, entityId);
    }

    public static JSONObject getFormAsJson(String formName) throws Exception {
        return FormUtils.getInstance(AGYWLibrary.getInstance().context().applicationContext()).getFormJson(formName);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void removeOptionIfNotInKeys(JSONObject formQuestion, List<String> keys) throws Exception {
        if (keys.isEmpty() || (keys.size() == 1 && StringUtils.isBlank(keys.get(0)))) {
            return;
        }
        //from the form question, get a jsonArray of options
        JSONArray options = formQuestion.getJSONArray("options");

        // Create a new JSONArray which will contain the new options
        JSONArray newOptions = new JSONArray();

        int optionsLength = options.length();
        int iterator;
        // from the options look for the option with the key that match and add the option to new options
        for (iterator = 0; iterator < optionsLength; iterator++) {
            JSONObject option = options.getJSONObject(iterator);
            // this will get keys that doesn't match remove it from the list
            if (keys.contains(option.getString("key"))) {
                newOptions.put(option);
            }
        }
        //clear the current options
        formQuestion.remove("options");
        //put new options
        formQuestion.put("options", newOptions);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void getBehavioralServicesForm(JSONObject form, int age, String enrolledPackage) throws Exception {
        JSONArray fields = form.getJSONObject(Constants.STEP_ONE).getJSONArray(JsonFormConstants.FIELDS);

        //update sbcc_intervention_provided
        JSONObject sbcc_intervention_provided = getFieldJSONObject(fields, "sbcc_intervention_provided");
        if (sbcc_intervention_provided != null && enrolledPackage.equalsIgnoreCase("dreams")) {
            removeOptionIfNotInKeys(sbcc_intervention_provided, age >= 15 ? Constants.DREAMS_PACKAGE.behavioral_services_15_24_keys : Constants.DREAMS_PACKAGE.behavioral_services_10_14_keys);
        } else if (sbcc_intervention_provided != null) {
            removeOptionIfNotInKeys(sbcc_intervention_provided, age >= 15 ? Constants.NON_DREAMS_PACKAGE.behavioral_services_15_24_keys : Collections.singletonList(""));

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void getStructuralServicesForm(JSONObject form, int age, String
            enrolledPackage) throws Exception {
        JSONArray fields = form.getJSONObject(Constants.STEP_ONE).getJSONArray(JsonFormConstants.FIELDS);

        //update economic_empowerment_education
        JSONObject economic_empowerment_education = getFieldJSONObject(fields, "economic_empowerment_education");
        if (economic_empowerment_education != null && enrolledPackage.equalsIgnoreCase("dreams")) {
            removeOptionIfNotInKeys(economic_empowerment_education, age >= 15 ? Constants.DREAMS_PACKAGE.structural_services_15_24_keys : Constants.DREAMS_PACKAGE.structural_services_10_14_keys);
            if (age < 15) {
                removeQuestion(fields, "entrepreneurial_tools");
                removeQuestion(fields, "given_capital");
            }
        } else if (economic_empowerment_education != null) {
            //catch for non dreams
            removeOptionIfNotInKeys(economic_empowerment_education, age >= 15 ? Constants.NON_DREAMS_PACKAGE.structural_services_15_24_keys : Collections.singletonList(""));
            if(age < 10) {
                removeQuestion(fields, "empower_girls");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void removeQuestion(JSONArray fields, String key) throws JSONException {
        Integer position = getFieldJSONObjectPosition(fields, key);
        if (position != null)
            fields.remove(position);
    }

    public static Integer getFieldJSONObjectPosition(JSONArray fields, String key) throws JSONException {
        int length = fields.length();
        for (int iterator = 0; iterator < length; iterator++) {
            JSONObject option = fields.getJSONObject(iterator);

            if (option.getString("key").equals(key)) {
                return iterator;
            }
        }

        return null;
    }

}
