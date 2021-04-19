package com.playbench;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//오레오 버전 이상은 채널로 만들어 줘야 한다.
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            makeNotificationChannel("home","home","",NotificationManager.IMPORTANCE_HIGH, defaultSoundUri);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, String description,int importance, Uri soundUri)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(id,name,importance);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.setShowBadge(true);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build();

        long[] pattern = {0,500,0,500};

        channel.setVibrationPattern(pattern);
        channel.enableVibration(true);
        channel.setDescription(description);
        channel.setSound(soundUri,audioAttributes);

        notificationManager.createNotificationChannel(channel);
    }
}
