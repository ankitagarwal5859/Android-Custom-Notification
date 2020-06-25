package com.dassault.medidatanotificationdemo.notification.model;

public class CustomNotificationAnyTimeFormTemplate {
    private String formOId;
    private String weekDayRecurrences;
    private String deliveryTime;
    private String message;
    private long openTime;
    private long closeTime;

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

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
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
