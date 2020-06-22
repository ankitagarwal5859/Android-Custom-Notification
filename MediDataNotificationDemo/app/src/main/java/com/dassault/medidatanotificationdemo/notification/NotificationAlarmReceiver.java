package com.dassault.medidatanotificationdemo.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Random;

public class NotificationAlarmReceiver extends BroadcastReceiver {
    private final static String TAG = NotificationAlarmReceiver.class.getSimpleName();
    public final static String ACTION = "com.dassault.medidatanotificationdemo.notification.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getExtras().getString("Message");
        Log.d(TAG, "Alarm receiver called!!!!" + message);
        Random r = new Random();
        int notificationId = r.nextInt(
                100) + 1;
        NotificationUtils.createNotification(context, "1", "Testing", "Medidata", "Medidata", message, notificationId);

    }
}
