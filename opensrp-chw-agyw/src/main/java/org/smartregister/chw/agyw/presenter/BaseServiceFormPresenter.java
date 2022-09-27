package org.smartregister.chw.agyw.presenter;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.agyw.R;
import org.smartregister.chw.agyw.contract.ServicesFormsContract;

import java.lang.ref.WeakReference;

public class BaseServiceFormPresenter implements ServicesFormsContract.Presenter, ServicesFormsContract.InteractorCallBack {

    public static final String TAG = BaseServiceFormPresenter.class.getName();
    private final ServicesFormsContract.Interactor interactor;
    protected WeakReference<ServicesFormsContract.View> viewReference;
    protected ServicesFormsContract.Model model;

    public BaseServiceFormPresenter(ServicesFormsContract.View view, ServicesFormsContract.Model model, ServicesFormsContract.Interactor interactor) {
        viewReference = new WeakReference<>(view);
        this.interactor = interactor;
        this.model = model;
    }

    @Override
    public void startForm(String formName, String entityId, String metadata, String currentLocationId, int age) throws Exception {
        if (StringUtils.isBlank(entityId)) {
            return;
        }

        JSONObject form = model.getFormAsJson(formName, entityId, currentLocationId, age);
        getView().startFormActivity(form);
    }

    private ServicesFormsContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

    @Override
    public void saveForm(String jsonString) {
        try {
            getView().displayToast(getView().getContext().getString(R.string.saving_dialog_title));
            interactor.saveForm(jsonString, this);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void onRegistrationSaved() {

    }
}
