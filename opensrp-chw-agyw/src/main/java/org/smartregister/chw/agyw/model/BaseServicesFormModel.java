package org.smartregister.chw.agyw.model;

import android.os.Build;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.agyw.contract.ServicesFormsContract;
import org.smartregister.chw.agyw.dao.AGYWDao;
import org.smartregister.chw.agyw.util.AGYWJsonFormUtils;
import org.smartregister.chw.agyw.util.Constants;

import androidx.annotation.RequiresApi;

public class BaseServicesFormModel implements ServicesFormsContract.Model {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId, int age) throws Exception {
        JSONObject jsonObject = AGYWJsonFormUtils.getFormAsJson(formName);
        AGYWJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);
        String enrolledPackage = AGYWDao.getEnrolledProgram(entityId);
        JSONObject global = jsonObject.getJSONObject("global");
        if (global != null) {
            global.put("age", age);
        }

        if (formName.equals(Constants.FORMS.AGYW_BEHAVIORAL) && StringUtils.isNotBlank(enrolledPackage)) {
            AGYWJsonFormUtils.getBehavioralServicesForm(jsonObject, age, enrolledPackage);
        }

        if (formName.equals(Constants.FORMS.AGYW_STRUCTURAL) && StringUtils.isNotBlank(enrolledPackage)) {
            AGYWJsonFormUtils.getStructuralServicesForm(jsonObject, age, enrolledPackage);
        }
        return jsonObject;
    }
}
