package org.smartregister.chw.agyw.interactor;

import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.agyw.contract.AGYWRegisterContract;
import org.smartregister.chw.agyw.util.AppExecutors;
import org.smartregister.chw.agyw.util.AGYWUtil;

public class BaseAGYWRegisterInteractor implements AGYWRegisterContract.Interactor {

    private AppExecutors appExecutors;

    @VisibleForTesting
    BaseAGYWRegisterInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseAGYWRegisterInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(final String jsonString, final AGYWRegisterContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            try {
                AGYWUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(() -> callBack.onRegistrationSaved());
        };
        appExecutors.diskIO().execute(runnable);
    }
}
