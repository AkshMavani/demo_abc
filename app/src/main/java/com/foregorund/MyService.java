package com.foregorund;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.demo_full.R;

public class MyService extends Service {
    private static final String TAG = "MyService";
    public static boolean isServiceRunning;
    private static final String CHANNEL_ID = "NOTIFICATION_CHANNEL";

    private Handler handler;
    private Runnable logRunnable;

    public MyService() {
        Log.d(TAG, "constructor called");
        isServiceRunning = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");
        createNotificationChannel();
        isServiceRunning = true;

        // Initialize Handler and Runnable for logging
        handler = new Handler();
        logRunnable = new Runnable() {
            @Override
            public void run() {
                if (isServiceRunning) {
                    Log.d(TAG, "Service is running...");
                    handler.postDelayed(this, 1000); // Schedule next log after 1 second
                }
            }
        };
        handler.post(logRunnable); // Start logging
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");

        Intent notificationIntent = new Intent(this, MainActivity21.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service is Running")
                .setContentText("Listening for Screen Off/On events")
                .setSmallIcon(R.drawable.ic_album)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .build();

        startForeground(1, notification);
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String appName = getString(R.string.app_name);
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    appName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy called");
        isServiceRunning = false;

        // Stop the handler and log runnable
        if (handler != null && logRunnable != null) {
            handler.removeCallbacks(logRunnable);
        }

        stopForeground(true);

        // Restart service via broadcast
        Intent broadcastIntent = new Intent(this, MyReceiver.class);
        sendBroadcast(broadcastIntent);

        super.onDestroy();
    }
}
