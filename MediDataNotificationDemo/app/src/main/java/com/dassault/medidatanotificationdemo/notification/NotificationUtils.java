package com.dassault.medidatanotificationdemo.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dassault.medidatanotificationdemo.R;

public class NotificationUtils {
    private static final String GROUP_KEY = "com.android.example.medidata";

    /**
     * @param context           context of class
     * @param chanelId          To create specific channel in settings
     * @param chanelName
     * @param chanelDescription
     * @param title             notification title on notification try
     * @param message           notification content
     * @param notificationId
     */
    public static void createNotification(Context context, String chanelId, String chanelName, String chanelDescription, String title, String message, int notificationId) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(chanelId,
                    chanelName,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(chanelDescription);
            channel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(context, chanelId)
                .setSmallIcon(R.drawable.ds1) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)
                .setGroup(GROUP_KEY)// message for notification
                .setAutoCancel(true).build(); // clear notification after click
        groupNotification(context, notification, notificationId);

    }

    public static void groupNotification(Context context, Notification notification, int notificationId) {
        int SUMMARY_ID = 0;
        Notification summaryNotification =
                new NotificationCompat.Builder(context, "1")
                        .setContentTitle("Notification :")
                        //set content text to support devices running API level < 24
                        .setContentText("Two new messages")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        //build summary info into InboxStyle template
                        .setStyle(new NotificationCompat.InboxStyle()

                                .setSummaryText("anagarwal@mdsol.com"))
                        //specify which group this notification belongs to
                        .setGroup(GROUP_KEY)
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, notification);
        notificationManager.notify(SUMMARY_ID, summaryNotification);

        /*Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(notificationId, mBuilder.build());*/
    }
}
