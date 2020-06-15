package com.dassault.medidatanotificationdemo.notification.model;

public class CustomNotification {
    private int percentage;
    private String message;
    private String startDate;
    private String endDate;
    private String completeDate;
    private boolean isAlarmTriggered;
    private long alarmTimestamp;


    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomNotification(int percentage, String message, String startDate, String endDate, boolean isAlarmTriggered) {
        this.percentage = percentage;
        this.message = message;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAlarmTriggered = isAlarmTriggered;
    }

    public CustomNotification() {
        super();
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public boolean isAlarmTriggered() {
        return isAlarmTriggered;
    }

    public void setAlarmTriggered(boolean alarmTriggered) {
        isAlarmTriggered = alarmTriggered;
    }

    public long getAlarmTimestamp() {
        return alarmTimestamp;
    }

    public void setAlarmTimestamp(long alarmTimestamp) {
        this.alarmTimestamp = alarmTimestamp;
    }

    @Override
    public String toString() {
        return "CustomNotification{" +
                "percentage=" + percentage +
                ", message='" + message + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", completeDate='" + completeDate + '\'' +
                ", isAlarmTriggered=" + isAlarmTriggered +
                ", alarmTimestamp=" + alarmTimestamp +
                '}';
    }
}
