package org.smartregister.chw.agyw.interactor;

import org.smartregister.chw.agyw.contract.ServicesFormsContract;
import org.smartregister.chw.agyw.util.AGYWUtil;
import org.smartregister.chw.agyw.util.AppExecutors;

import androidx.annotation.VisibleForTesting;

public class BaseServicesFormInteractor implements ServicesFormsContract.Interactor {

    private final AppExecutors appExecutors;

    @VisibleForTesting
    BaseServicesFormInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseServicesFormInteractor() {
        this(new AppExecutors());
    }


    @Override
    public void saveForm(String jsonString, ServicesFormsContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            try {
                AGYWUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(callBack::onRegistrationSaved);
        };
        appExecutors.diskIO().execute(runnable);
    }
}
