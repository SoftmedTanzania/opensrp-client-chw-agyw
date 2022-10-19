package org.smartregister.chw.agyw.util;

import java.util.Arrays;
import java.util.List;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";
    String STEP_ONE = "step1";
    String STEP_TWO = "step2";
    String STEP_THREE = "step3";
    String STEP_FOUR = "step4";
    String STEP_FIVE = "step5";
    String STEP_SIX = "step6";
    String STEP_SEVEN = "step7";
    String STEP_EIGHT = "step8";
    String STEP_NINE = "step9";
    String STEP_TEN = "step10";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";
    }

    interface EVENT_TYPE {
        String AGYW_REGISTRATION = "AGYW Registration";
        String AGYW_FOLLOW_UP_VISIT = "Agyw Followup";
        String AGYW_BIO_MEDICAL_VISIT = "AGYW Bio Medical Services";
        String AGYW_BEHAVIORAL_VISIT = "AGYW Behavioral Services";
        String AGYW_STRUCTURAL_VISIT = "AGYW Structural Services";
        String AGYW_GRADUATE_SERVICES = "AGYW GRADUATE SERVICES";
    }

    interface FORMS {
        String AGYW_REGISTRATION = "agyw_screening";
        String AGYW_FOLLOWUP = "agyw_followup";
        String AGYW_BIO_MEDICAL = "agyw_bio_medical_services";
        String AGYW_BEHAVIORAL = "agyw_behavioral_services";
        String AGYW_STRUCTURAL = "agyw_structural_services";
    }

    interface TABLES {
        String AGYW_REGISTER = "ec_agyw_register";
        String AGYW_FOLLOW_UP = "ec_agyw_followup";
        String FAMILY_MEMBER_TABLE = "ec_family_member";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String AGYW_FORM_NAME = "AGYW_FORM_NAME";

        String AGE = "AGE";
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

    interface SERVICES {
        String AGYW_BIO_MEDICAL_SERVICES = "AGYW BIO MEDICAL SERVICES";
        String AGYW_BEHAVIORAL_SERVICES = "AGYW BEHAVIORAL SERVICES";
        String AGYW_STRUCTURAL_SERVICES = "AGYW STRUCTURAL SERVICES";
    }

    interface JSON_FORM_KEY {
        String UIC_ID = "uic_id";
    }

    interface DREAMS_PACKAGE {
        List<String> behavioral_services_15_24_keys = Arrays.asList("gender_and_sex_education",
                "reproductive_health",
                "effective_communication",
                "sti_hiv_aids",
                "family_planning",
                "good_relationships",
                "gender_based_violence",
                "sustainable_usage_condoms",
                "ctc_services",
                "prep_education",
                "pep_education",
                "tb_education",
                "education_hiv_testing",
                "provision_iec",
                "puberty_education_menstrual_hygiene",
                "life_skills");
        List<String> behavioral_services_10_14_keys = Arrays.asList("gender_and_sex_education",
                "reproductive_health",
                "sti_hiv_aids",
                "good_relationships",
                "gender_based_violence",
                "sustainable_usage_condoms",
                "puberty_education_menstrual_hygiene",
                "life_skills");
        List<String> structural_services_15_24_keys = Arrays.asList("group_entrepreneurial_skills",
                "practical_skills",
                "entrepreneurship_tools",
                "self_emp_and_emp",
                "saccos_vicoba",
                "savings_mgmt",
                "credit_mgmt",
                "planning_a_budget",
                "starting_a_business",
                "prepare_business_plan",
                "business_mgmt",
                "commercial_banking_services",
                "marketing_research",
                "financial_edu_and_investments");
        List<String> structural_services_10_14_keys = Arrays.asList("savings_mgmt",
                "planning_a_budget",
                "financial_edu_and_investments");

    }
}