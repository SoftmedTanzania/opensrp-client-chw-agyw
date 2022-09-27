package org.smartregister.chw.agyw.contract;

import android.content.Context;

import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;


public interface ServicesFormsContract {
    interface View {
        Presenter presenter();

        Form getFormConfig();
        void startFormActivity(String formName, String baseEntityId, String metadata, int age);

        void startFormActivity(JSONObject jsonForm);

        void displayToast(String message);

        Context getContext();

        void initializePresenter();
    }

    interface Presenter {

        void startForm(String formName, String entityId, String metadata, String currentLocationId, int age) throws Exception;

        void saveForm(String jsonString);

    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId, int age) throws Exception;

    }

    interface Interactor {

        void saveForm(String jsonString, final InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onRegistrationSaved();

    }
}
