package com.carlospinan.popularmovies;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author Carlos Piñan
 */
public class PopularMoviesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
