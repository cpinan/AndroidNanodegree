package com.example.android.sunshine.app.sync;

import android.graphics.Bitmap;

import com.google.android.gms.wearable.DataMap;

/**
 * @author Carlos Piñan
 */
public interface IWeatherListener {

    void sendWeatherData(DataMap config);

    void sendWeatherIcon(Bitmap bitmap);

    void sendWeather(Bitmap bitmap, DataMap config);
}
