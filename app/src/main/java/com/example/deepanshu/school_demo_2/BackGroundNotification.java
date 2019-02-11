package com.example.deepanshu.school_demo_2;

import android.app.Service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.firestore.DocumentReference;

public class BackGroundNotification extends Service {
private static SharedPreferences mypref;
private static DocumentReference teacherReference,studentReference;
private NotificationManagerCompat notificationManagerCompat;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }
    public static void setReferences(DocumentReference  s,DocumentReference t)
    {
        teacherReference = t;
        studentReference = s;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {

        if(teacherReference==null||studentReference==null)
        {
            stopSelf();
        }
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
