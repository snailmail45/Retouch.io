package com.evan.scanenhancer.util;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.evan.scanenhancer.Constants;
import com.evan.scanenhancer.R;

public class NotificationUtil {
    //This one is for identifying multiple notifications happening
    //If I send one about shopping with code 0 and another one with code 0, it would update the code 0 one.
    private static final int NOTIFICATION_ID = 0;

    //The channel id is using a String instead of an int to identify the channel.
    //A channel is the type of notification. e.g "Friend request", "Message", "Download complete"
    //They could even have the same name "Download complete" and "Download complete" if the String channel id is different
    private static final String CHANNEL_DOWNLOAD_COMPLETE = "CHANNEL_DOWNLOAD_COMPLETE";

    //Notification for transferring the image template from assets to external downloads dir
    public static void createDownloadComplete(Context context, String fileName){
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                Constants.RC_ACTION_VIEW_DOWNLOAD,
                intent,
                PendingIntent.FLAG_ONE_SHOT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_DOWNLOAD_COMPLETE)
                .setSmallIcon(R.drawable.ic_download_finished)
                .setContentTitle(fileName)
                .setContentText("Download complete.")
                .setPriority(NotificationCompat.PRIORITY_LOW) //No sound alert
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        //Oreo and above requires notification channels, other wise notification won't appear.
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_DOWNLOAD_COMPLETE,
                    "Download complete", //Name of the notification
                    NotificationManager.IMPORTANCE_LOW //No sound alert
            );
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
