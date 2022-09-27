package org.smartregister.chw.agyw.model;

import org.json.JSONObject;
import org.smartregister.chw.agyw.contract.AGYWRegisterContract;
import org.smartregister.chw.agyw.util.AGYWJsonFormUtils;

public class BaseAGYWRegisterModel implements AGYWRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = AGYWJsonFormUtils.getFormAsJson(formName);
        AGYWJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId, int age) throws Exception {
        JSONObject jsonObject = AGYWJsonFormUtils.getFormAsJson(formName);
        AGYWJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);
        JSONObject global = jsonObject.getJSONObject("global");
        if (global != null) {
            global.put("age", age);
        }

        return jsonObject;
    }

}
