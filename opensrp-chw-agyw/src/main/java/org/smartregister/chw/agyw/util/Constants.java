package org.smartregister.chw.agyw.util;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";
    String STEP_ONE = "step1";
    String STEP_TWO = "step2";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";
    }

    interface EVENT_TYPE {
        String AGYW_CONFIRMATION = "Agyw Confirmation";
        String AGYW_FOLLOW_UP_VISIT = "Agyw Follow-up Visit";
    }

    interface FORMS {
        String AGYW_REGISTRATION = "agyw_confirmation";
        String AGYW_FOLLOW_UP_VISIT = "agyw_followup_visit";
    }

    interface TABLES {
        String AGYW_CONFIRMATION = "ec_agyw_confirmation";
        String AGYW_FOLLOW_UP = "ec_agyw_follow_up_visit";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String AGYW_FORM_NAME = "AGYW_FORM_NAME";

    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String AGYW_CONFIRMATION = "agyw_confirmation";
    }

    interface AGYW_MEMBER_OBJECT {
        String MEMBER_OBJECT = "memberObject";
    }

}