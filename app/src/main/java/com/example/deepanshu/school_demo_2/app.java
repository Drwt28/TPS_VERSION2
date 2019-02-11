package com.example.deepanshu.school_demo_2;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class app extends Application {

    public static final String CHANNEL_ID_1="channel 1";
    public static final String CHANNEL_ID_2="channel 2";
    @Override
    public void onCreate() {
        super.onCreate();


        createNotificationChannels();

    }

    public void createNotificationChannels(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_1,
                    "Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Notification Channels");


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }



    }
}
