package com.example.gallery.util;

import android.app.Application;

public class BaseApplication extends Application {
    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        instance = this;
        BaseManager.init();
    }
}
