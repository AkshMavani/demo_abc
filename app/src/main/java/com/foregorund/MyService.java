package com.foregorund;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.demo_full.R;
import com.foregorund.adapter.ScreenReceiver;



public class MyService extends Service {
    private static final String TAG = "MyService";
    private static final String CHANNEL_ID = "NOTIFICATION_CHANNEL";
    private boolean isWindowOpen = false;
    public static boolean isServiceRunning;
    private boolean isNotificationCreated = false;
    private Handler handler;
    private Runnable periodicRunnable;
    private ScreenReceiver receiver;

    public MyService() {
        Log.d(TAG, "constructor called");
        isServiceRunning = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");

        // Register screen on/off receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        receiver = new ScreenReceiver();
        registerReceiver(receiver, filter);

        createNotificationChannel();
        isServiceRunning = true;

        // Initialize periodic task
        handler = new Handler();
        periodicRunnable = new Runnable() {
            @Override
            public void run() {
                if (isServiceRunning) {
                    Log.d(TAG, "Periodic task running...");
                    handler.postDelayed(this, 5000); // Run every 5 seconds
                }
            }
        };
        handler.post(periodicRunnable);

        // Start foreground notification
        startForegroundNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");

        boolean isWindowClosed = intent.getBooleanExtra("window_closed", false);
        boolean isScreenOn = intent.getBooleanExtra("a", false);

        Log.e(TAG, "onStartCommand: isScreenOn = " + isScreenOn);
        Log.e(TAG, "onStartCommand: isWindowClosed = " + isWindowClosed);

        if (isWindowClosed) {
            Log.e(TAG, "Window closed");
            isWindowOpen = false;
        }

        if (isScreenOn && !isWindowOpen) {
            Log.e(TAG, "Opening Window on screen ON");
            Window window = new Window(this);
            window.open();
            isWindowOpen = true;
        }

        if (!isNotificationCreated) {
            startForegroundNotification();
        }

        return START_STICKY;
    }

    private void startForegroundNotification() {
        if (isNotificationCreated) return; // Prevent multiple notifications

        Intent notificationIntent = new Intent(this, MainActivity21.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service Running")
                .setContentText("Listening for events")
                .setSmallIcon(R.drawable.ic_album)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOngoing(true) // Prevent swipe dismissal
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .setAutoCancel(false) // Prevent dismissal on click
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // Show on lock screen

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        startForeground(1, notification);
        isNotificationCreated = true;
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

        if (handler != null && periodicRunnable != null) {
            handler.removeCallbacks(periodicRunnable);
        }

        stopForeground(true);

        // Restart service via broadcast
        Intent broadcastIntent = new Intent(this, MyReceiver.class);
        sendBroadcast(broadcastIntent);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


/*public class MyService extends Service {
    ScreenReceiver receiver;
    boolean isWindowOpen = false;
    private static final String TAG = "MyService";
    public static boolean isServiceRunning;
    private static final String CHANNEL_ID = "NOTIFICATION_CHANNEL";

    private Handler handler;
    private Runnable periodicRunnable;
    private boolean isNotificationCreated = false; // Flag to check if notification is already created

    public MyService() {
        Log.d(TAG, "constructor called");
        isServiceRunning = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        receiver = new ScreenReceiver();
        registerReceiver(receiver, filter);

        createNotificationChannel();
        isServiceRunning = true;

        // Initialize the Handler and Runnable for periodic calls
        handler = new Handler();
        periodicRunnable = new Runnable() {
            @Override
            public void run() {
                if (isServiceRunning) {
                    Log.d(TAG, "Periodic task running...");
                    handler.postDelayed(this, 5000); // Schedule the next run (5 seconds)
                }
            }
        };
        handler.post(periodicRunnable); // Start the periodic calls

        startForegroundNotification(); // Create and start the notification
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");

        boolean isWindowClosed = intent.getBooleanExtra("window_closed", false);
        boolean isScreenOn = intent.getBooleanExtra("a", false);

        Log.e(TAG, "onStartCommand: isScreenOn = " + isScreenOn);
        Log.e(TAG, "onStartCommand: isWindowClosed = " + isWindowClosed);

        if (isWindowClosed) {
            Log.e(TAG, "Window closed");
            isWindowOpen = false;
        }

        if (isScreenOn && !isWindowOpen) {
            Log.e(TAG, "Opening Window on screen ON");
            Window window = new Window(this);
            window.open();
            isWindowOpen = true;
        }

        // No need to recreate the notification, only start the foreground service if not started
        if (!isNotificationCreated) {
            startForegroundNotification();
        }

        return START_STICKY;
    }

    private void startForegroundNotification() {
        if (isNotificationCreated) return; // Prevent multiple notifications

        Intent notificationIntent = new Intent(this, MainActivity21.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service Running")
                .setContentText("Listening for events")
                .setSmallIcon(R.drawable.ic_album)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_MAX) // Ensures high visibility
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOngoing(true) // Prevents swipe dismissal
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .setAutoCancel(false) // Prevents dismissal on click
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // Show on lock screen

        Notification notification = builder.build();

        // Ensures the notification cannot be dismissed
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        // Start the service as a foreground service
        startForeground(1, notification);
        isNotificationCreated = true; // Set flag after creating the notification
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

        // Stop the handler and periodic runnable
        if (handler != null && periodicRunnable != null) {
            handler.removeCallbacks(periodicRunnable);
        }

        stopForeground(true);

        // Restart service via broadcast
        Intent broadcastIntent = new Intent(this, MyReceiver.class);
        sendBroadcast(broadcastIntent);

        super.onDestroy();
    }
}*/

//======================

/*
package com.foregorund;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.demo_full.R;
import com.foregorund.adapter.ScreenReceiver;

public class MyService extends Service {
    ScreenReceiver receiver;
    boolean isWindowOpen = false;
    private static final String TAG = "MyService";
    public static boolean isServiceRunning;
    private static final String CHANNEL_ID = "NOTIFICATION_CHANNEL";

    private Handler handler;
    private Runnable periodicRunnable;

    public MyService() {
        Log.d(TAG, "constructor called");
        isServiceRunning = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        receiver = new ScreenReceiver();
        registerReceiver(receiver, filter);

        createNotificationChannel();
        isServiceRunning = true;

        // Initialize the Handler and Runnable for periodic calls
        handler = new Handler();
        periodicRunnable = new Runnable() {
            @Override
            public void run() {
                if (isServiceRunning) {
                    Log.d(TAG, "Periodic call to onStartCommand");
                    Intent intent = new Intent(MyService.this, MyService.class);
                    intent.putExtra("periodic_call", true);
                    startService(intent); // Trigger onStartCommand
                    handler.postDelayed(this, 5000); // Schedule the next run (5 seconds)
                }
            }
        };
        handler.post(periodicRunnable); // Start the periodic calls
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");

        boolean isWindowClosed = intent.getBooleanExtra("window_closed", false);
        boolean isScreenOn = intent.getBooleanExtra("a", false);
        boolean isPeriodicCall = intent.getBooleanExtra("periodic_call", false);

        Log.e(TAG, "onStartCommand: isScreenOn = " + isScreenOn);
        Log.e(TAG, "onStartCommand: isWindowClosed = " + isWindowClosed);
        Log.e(TAG, "onStartCommand: isPeriodicCall = " + isPeriodicCall);

        if (isWindowClosed) {
            Log.e(TAG, "Window closed");
            isWindowOpen = false;
        }

        if (isScreenOn && !isWindowOpen) {
            Log.e(TAG, "Opening Window on screen ON");
            Window window = new Window(this);
            window.open();
            isWindowOpen = true;
        }

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity21.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

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

        // Stop the handler and periodic runnable
        if (handler != null && periodicRunnable != null) {
            handler.removeCallbacks(periodicRunnable);
        }

        stopForeground(true);

        // Restart service via broadcast
        Intent broadcastIntent = new Intent(this, MyReceiver.class);
        sendBroadcast(broadcastIntent);

        super.onDestroy();
    }
}
*/

//-========================================

//package com.foregorund;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.core.app.NotificationCompat;
//
//import com.example.demo_full.R;
//import com.foregorund.adapter.ScreenReceiver;
//
//public class MyService extends Service {
//    ScreenReceiver  reciver;
//    boolean  isWindowOpen = false;
//    private static final String TAG = "MyService";
//    public static boolean isServiceRunning;
//    private static final String CHANNEL_ID = "NOTIFICATION_CHANNEL";
//
//    private Handler handler;
//    private Runnable logRunnable;
//
//    public MyService() {
//        Log.d(TAG, "constructor called");
//        isServiceRunning = false;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.d(TAG, "onCreate called");
//        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(reciver, filter);
//
//        createNotificationChannel();
//        isServiceRunning = true;
//
//        // Initialize Handler and Runnable for logging
//        handler = new Handler();
//        logRunnable = new Runnable() {
//            @Override
//            public void run() {
//                if (isServiceRunning) {
//                    Log.d(TAG, "Service is running...");
//                    handler.postDelayed(this, 1000); // Schedule next log after 1 second
//                }
//            }
//        };
//        handler.post(logRunnable); // Start logging
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand called");
//        boolean isWindowClosed = intent.getBooleanExtra("window_closed", false);
//        boolean isScreenOn = intent.getBooleanExtra("a", false);
//        Log.e("isss", "onStartCommand: "+isScreenOn );
//        Log.e("isss", "onStartCommand: "+isWindowClosed );
//
//        if (isWindowClosed) {
//            Log.e("Service", "Window closed");
//            isWindowOpen = false;
//        }
//
//        if (isScreenOn && !isWindowOpen) {
//            Log.e("Service", "Opening Window on screen ON");
//            Window window = new Window(this);
//            window.open();
//            isWindowOpen = true;
//        }
//
//        createNotificationChannel();
//        Intent notificationIntent = new Intent(this, MainActivity21.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Service is Running")
//                .setContentText("Listening for Screen Off/On events")
//                .setSmallIcon(R.drawable.ic_album)
//                .setContentIntent(pendingIntent)
//                .setColor(getResources().getColor(R.color.colorPrimary))
//                .build();
//
//        startForeground(1, notification);
//        return START_STICKY;
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String appName = getString(R.string.app_name);
//            NotificationChannel serviceChannel = new NotificationChannel(
//                    CHANNEL_ID,
//                    appName,
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(serviceChannel);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.d(TAG, "onDestroy called");
//        isServiceRunning = false;
//
//        // Stop the handler and log runnable
//        if (handler != null && logRunnable != null) {
//            handler.removeCallbacks(logRunnable);
//        }
//
//        stopForeground(true);
//
//        // Restart service via broadcast
//        Intent broadcastIntent = new Intent(this, MyReceiver.class);
//        sendBroadcast(broadcastIntent);
//
//        super.onDestroy();
//    }
//}
