package com.dassault.medidatanotificationdemo.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dassault.medidatanotificationdemo.notification.model.CustomNotification;
import com.dassault.medidatanotificationdemo.notification.model.NotificationDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class NotificationService extends IntentService {
    private final static String TAG = NotificationService.class.getSimpleName();
    private Map<String, ArrayList<CustomNotification>> notificationDataSet;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificationService(String name) {
        super(name);
    }

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Sync Service onHandleIntent called");
        notificationDataSet = NotificationDataSet.notificationMap;
        for (Map.Entry<String, ArrayList<CustomNotification>> entry : notificationDataSet.entrySet()) {
            if (entry.getValue().size() > 0) {
                for (CustomNotification obj : entry.getValue()) {
                    if (!obj.isAlarmTriggered()) {
                        Date completeDate = DateUtils.getDateAfterCompletion(obj.getStartDate(), obj.getEndDate(), obj.getPercentage());
                        String finalDate = DateUtils.format.format(completeDate);
                        obj.setAlarmTimestamp(completeDate.getTime());
                        Log.d(TAG, "Alarm is triggered for  " + entry.getKey() + " Percentage " + obj.getPercentage() + " It will show at " + finalDate);
                        setAlarm(obj);
                    } else {
                        Log.d(TAG, "Alarm is already triggered for " + entry.getKey() + " Percentage " + obj.getPercentage());
                    }
                }
            }
        }


    }

    public void setAlarm(CustomNotification objCustomNotification) {
        objCustomNotification.setAlarmTriggered(true);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, NotificationAlarmReceiver.class);
        alarmIntent.putExtra("Message", objCustomNotification.getMessage());
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, objCustomNotification.getAlarmTimestamp(), 0, pendingIntent);// Millisec * Second * Minute
    }
}
