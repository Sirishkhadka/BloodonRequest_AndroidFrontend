package com.example.bloodonrequest.createChannel;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class CreateChannel {

    Context context;

    public static final String NOTIFICATION_REPLY = "NotificationReply";

    public final static String CHANNEL_1 = "Channel1";
    public final static String CHANNEL_2 = "Channek2";

    //constructor
    public CreateChannel(Context context) {
        this.context = context;
    }


    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel one");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );

            channel2.setDescription("This is channel two");


            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
