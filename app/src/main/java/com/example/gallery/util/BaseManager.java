package com.example.gallery.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class BaseManager {
    public static int heightPixels = 1920;
    public static int heightRealPixels = 1920;
    private static OkHttpClient okHttpClient = null;
    public static int widthPixels = 1080;
    public static int widthRealPixels = 1080;

    /* JADX WARN: Type inference failed for: r0v0, types: [vn.demo.base.manager.BaseManager$1] */
    public static void init() {
        new Thread() { // from class: vn.demo.base.manager.BaseManager.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                super.run();
                new File(BaseManager.appContext().getFilesDir().getPath() + "/txt/").mkdirs();
                WindowManager windowManager = (WindowManager) BaseManager.appContext().getSystemService(Context.WINDOW_SERVICE);
                if (windowManager == null) {
                    return;
                }
                DisplayMetrics displayMetrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                BaseManager.widthPixels = displayMetrics.widthPixels;
                BaseManager.heightPixels = displayMetrics.heightPixels;
                windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
                BaseManager.widthRealPixels = displayMetrics.widthPixels;
                BaseManager.heightRealPixels = displayMetrics.heightPixels;
            }
        }.start();
    }

    public static OkHttpClient okHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().connectTimeout(10L, TimeUnit.SECONDS).writeTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build();
        }
        return okHttpClient;
    }

    public static Context appContext() {
        return BaseApplication.getInstance().getApplicationContext();
    }
}
