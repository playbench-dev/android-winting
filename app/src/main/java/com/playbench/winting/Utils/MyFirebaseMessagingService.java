package com.playbench.winting.Utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.playbench.winting.Activitys.MainActivity;
import com.playbench.winting.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.app.Notification.CATEGORY_MESSAGE;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyNotificationListener";


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    MediaPlayer player;
    NotificationCompat.Builder notificationBuilder;
    Notification notification;
    NotificationManager notificationManager;
    private static int notificationId = 0;
    MediaPlayer mediaPlayer;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

    }

    @Override
    public void onCreate() {

    }

    // [START receive_message]
    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = null;
        AudioAttributes audioAttributes;

        Log.i(TAG,"remoteMessage : " + remoteMessage.getData().toString());

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("android_channel_id",remoteMessage.getData().get("android_channel_id"));
        notificationIntent.putExtra("push", true);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
//        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , TAG);
        wakeLock.acquire(3000);

        PendingIntent pendingI = PendingIntent.getActivity(this, (int) (System.currentTimeMillis() / 1000),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap myBitmap = null;
        if (remoteMessage.getData().containsKey("image")){
            if (remoteMessage.getData().get("image").length() != 0)
                myBitmap = getBitmapFromURL(remoteMessage.getData().get("image"));
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, remoteMessage.getData().get("android_channel_id"));
        builder.setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("{Time to watch some cool stuff!}")
                .setContentTitle(remoteMessage.getData().get("title"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("body")))
                .setContentText(remoteMessage.getData().get("body"))
                .setContentInfo("INFO")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLargeIcon(myBitmap)
//                .setSound(defaultSoundUri)
                .setContentIntent(pendingI);

        if (myBitmap != null){
            builder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(myBitmap)
                    .bigLargeIcon(null));
        }
        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_winting_logo); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            String channelId = remoteMessage.getData().get("android_channel_id");
            CharSequence channelName = remoteMessage.getData().get("android_channel_id");
            String description = "";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

            try {
                audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();
//                channel.setSound(defaultSoundUri, audioAttributes);
                notificationManager.createNotificationChannel(channel);
            } catch (Exception e) {
                e.printStackTrace();
            }

            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
//            assert notificationManager != null;
            if (!notificationManager.getNotificationChannels().contains(channel)) {
                notificationManager.createNotificationChannel(channel);
            }

        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        notificationManager.notify((int) (System.currentTimeMillis() / 1000), builder.build());

        wakeLock.release();
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
