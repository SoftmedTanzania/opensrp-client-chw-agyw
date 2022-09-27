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

}
