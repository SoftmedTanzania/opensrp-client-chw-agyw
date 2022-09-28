package org.smartregister.chw.agyw.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.agyw.contract.AGYWProfileContract;
import org.smartregister.chw.agyw.dao.AGYWDao;
import org.smartregister.chw.agyw.domain.MemberObject;
import org.smartregister.chw.agyw.util.AppExecutors;
import org.smartregister.chw.agyw.util.AGYWUtil;
import org.smartregister.domain.AlertStatus;

import java.util.Date;

public class BaseAGYWProfileInteractor implements AGYWProfileContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseAGYWProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseAGYWProfileInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void refreshProfileInfo(MemberObject memberObject, AGYWProfileContract.InteractorCallBack callback) {
        Runnable runnable = () -> appExecutors.mainThread().execute(() -> {
            callback.refreshFamilyStatus(AlertStatus.normal);
            callback.refreshMedicalHistory(true);
            callback.refreshUpComingServicesStatus("Agyw Visit", AlertStatus.normal, new Date());
        });
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRegistration(final String jsonString, final AGYWProfileContract.InteractorCallBack callback) {

        Runnable runnable = () -> {
            try {
                AGYWUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void graduateServices(String baseEntityId) {
        Runnable runnable = () -> {
            try {
                AGYWUtil.crateGraduateFromServicesEvent(baseEntityId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }
}
