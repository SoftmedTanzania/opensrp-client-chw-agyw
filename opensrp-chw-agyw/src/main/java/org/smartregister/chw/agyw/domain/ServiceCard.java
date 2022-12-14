package org.smartregister.chw.agyw.domain;

public class ServiceCard {
    private int background;

    private Integer actionItems;

    private String id;

    private String serviceName;

    private String serviceStatus;

    private Integer serviceIcon;

    private Integer visitsCount;

    private String eventServiceName;

    private String formName;

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public Integer getActionItems() {
        return actionItems;
    }

    public void setActionItems(int actionItems) {
        this.actionItems = actionItems;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return id;
    }

    public Integer getServiceIcon() {
        return serviceIcon;
    }

    public void setServiceIcon(Integer serviceIcon) {
        this.serviceIcon = serviceIcon;
    }

    public String getServiceEventName() {
        return eventServiceName;
    }

    public void setEventServiceName(String eventServiceName) {
        this.eventServiceName = eventServiceName;
    }

    public Integer getVisitsCount() {
        return visitsCount;
    }

    public void setVisitsCount(Integer visitsCount) {
        this.visitsCount = visitsCount;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}