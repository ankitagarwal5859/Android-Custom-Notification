package com.dassault.medidatanotificationdemo.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Random;

public class NotificationAlarmReceiver extends BroadcastReceiver {
    private final static String TAG = NotificationAlarmReceiver.class.getSimpleName();

    @Override

    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "Alarm receiver called!!!!");
        String message = "Schedule notification";

        Random r = new Random();
        int notificationId = r.nextInt(100) + 1;
        NotificationUtils.createNotification(context, "1", "Testing", "Medidata", "Medidata", message, notificationId);

    }
}