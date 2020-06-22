package com.dassault.medidatanotificationdemo.notification.model;

public class CustomNotificationAnyTimeFormTemplate {
    private String formOId;
    private String weekDayRecurrences;
    private String deliveryTime;
    private String message;

    public CustomNotificationAnyTimeFormTemplate() {
        super();
    }

    public CustomNotificationAnyTimeFormTemplate(String formOId, String weekDayRecurrences, String deliveryTime, String message) {
        this.formOId = formOId;
        this.weekDayRecurrences = weekDayRecurrences;
        this.deliveryTime = deliveryTime;
        this.message = message;
    }

    public String getFormOId() {
        return formOId;
    }

    public void setFormOId(String formOId) {
        this.formOId = formOId;
    }

    public String getWeekDayRecurrences() {
        return weekDayRecurrences;
    }

    public void setWeekDayRecurrences(String weekDayRecurrences) {
        this.weekDayRecurrences = weekDayRecurrences;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CustomNotificationAnyTimeFormTemplate{" +
                "formOId='" + formOId + '\'' +
                ", weekDayRecurrences='" + weekDayRecurrences + '\'' +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
