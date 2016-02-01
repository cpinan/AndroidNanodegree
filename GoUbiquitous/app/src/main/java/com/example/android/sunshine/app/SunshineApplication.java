package com.example.android.sunshine.app;

import android.app.Application;
import android.content.Context;

/**
 * @author Carlos Piñan
 */
public class SunshineApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
