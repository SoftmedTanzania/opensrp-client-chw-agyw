package org.smartregister.chw.agyw.contract;

import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterContract;

public interface AGYWRegisterContract {

    interface View extends BaseRegisterContract.View {
        Presenter presenter();

        void startFormActivity(String formName, String entityId, String metadata, int age) throws Exception;

        Form getFormConfig();
    }

    interface Presenter extends BaseRegisterContract.Presenter {

        void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception;

        void startForm(String formName, String entityId, String metadata, String currentLocationId, int age) throws Exception;

        void saveForm(String jsonString);

    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId, int age) throws Exception;

    }

    interface Interactor {

        void saveRegistration(String jsonString, final InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onRegistrationSaved();

    }
}
