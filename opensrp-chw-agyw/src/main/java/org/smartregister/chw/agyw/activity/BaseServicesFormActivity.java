package org.smartregister.chw.agyw.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.Context;
import org.smartregister.agyw.R;
import org.smartregister.chw.agyw.contract.ServicesFormsContract;
import org.smartregister.chw.agyw.interactor.BaseServicesFormInteractor;
import org.smartregister.chw.agyw.model.BaseServicesFormModel;
import org.smartregister.chw.agyw.presenter.BaseServiceFormPresenter;
import org.smartregister.chw.agyw.util.Constants;
import org.smartregister.util.Utils;
import org.smartregister.view.activity.SecuredActivity;

import androidx.annotation.Nullable;
import timber.log.Timber;

import static org.smartregister.chw.agyw.util.AGYWUtil.startClientProcessing;

public class BaseServicesFormActivity extends SecuredActivity implements ServicesFormsContract.View {

    protected String BASE_ENTITY_ID;
    protected String FORM_NAME;
    protected int AGE;
    protected ServicesFormsContract.Presenter presenter;

    public static void startMe(Activity activity, String formName, String baseEntityId, int age) {
        Intent intent = new Intent(activity, BaseServicesFormActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.AGYW_FORM_NAME, formName);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.AGE, age);

        activity.startActivity(intent);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        BASE_ENTITY_ID = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        FORM_NAME = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.AGYW_FORM_NAME);
        AGE = getIntent().getIntExtra(Constants.ACTIVITY_PAYLOAD.AGE, 0);
        onStartActivityWithAction();
    }

    @Override
    public void initializePresenter() {
        presenter = new BaseServiceFormPresenter(this, new BaseServicesFormModel(), new BaseServicesFormInteractor());
    }

    protected void onStartActivityWithAction() {
        if (FORM_NAME != null && BASE_ENTITY_ID != null) {
            startFormActivity(FORM_NAME, BASE_ENTITY_ID, null, AGE);
        }
    }

    @Override
    public ServicesFormsContract.Presenter presenter() {
        return presenter;
    }

    @Override
    public Form getFormConfig() {
        return null;
    }

    @Override
    protected void onCreation() {
        initializePresenter();
    }

    @Override
    public void startFormActivity(String formName, String entityId, String metaData, int age) {
        try {
            String locationId = Context.getInstance().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
            presenter().startForm(formName, entityId, metaData, locationId, age);
        } catch (Exception e) {
            Timber.e(e);
            displayToast(getString(R.string.error_unable_to_start_form));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE_GET_JSON) {

            try {
                String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
                JSONObject form = new JSONObject(jsonString);
                presenter().saveForm(form.toString());
            } catch (JSONException e) {
                Timber.e(e);
                displayToast(getString(R.string.error_unable_to_save_form));
            }
            displayToast(getString(R.string.form_saved));
            startClientProcessing();
            finish();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }

    @Override
    public void displayToast(String message) {
        Utils.showToast(this.getApplicationContext(), message);
    }

    @Override
    public android.content.Context getContext() {
        return this;
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, BaseServicesFormActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

        if (getFormConfig() != null) {
            intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, getFormConfig());
        }
        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected void onResumption() {
        //empty ===> on resumption
    }
}
