package com.example.android.sunshine.app.sync;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.sunshine.app.SunshineApplication;
import com.example.android.sunshine.app.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * @author Carlos Piñan
 */
public class WeatherWearableImplementation
        extends WearableListenerService
        implements IWeatherListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "WeatherWearableImp";

    private static WeatherWearableImplementation instance;
    private TodayWeatherTask todayWeatherTask;

    private GoogleApiClient googleApiClient;

    public WeatherWearableImplementation() {
        googleApiClient = new GoogleApiClient.Builder(SunshineApplication.getContext())
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    public static WeatherWearableImplementation get() {
        if (instance == null) {
            instance = new WeatherWearableImplementation();
        }
        return instance;
    }

    @Override
    public void sendWeatherData(DataMap config) {
        sendWeatherDataToWatch(config);
    }

    @Override
    public void sendWeatherIcon(Bitmap bitmap) {
        sendWeatherIconToWatch(bitmap);
    }

    @Override
    public void sendWeather(Bitmap bitmap, DataMap config) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/weather");
        putDataMapReq.getDataMap().putString(TodayWeatherTask.KEY_WEATHER_LOW_TEMP, config.getString(TodayWeatherTask.KEY_WEATHER_LOW_TEMP));
        putDataMapReq.getDataMap().putString(TodayWeatherTask.KEY_WEATHER_MAX_TEMP, config.getString(TodayWeatherTask.KEY_WEATHER_MAX_TEMP));
        putDataMapReq.getDataMap().putAsset(TodayWeatherTask.KEY_WEATHER_ICON, Utility.createAssetFromBitmap(bitmap));
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        // PendingResult<DataApi.DataItemResult> pendingResult =
//        Wearable.DataApi.putDataItem(googleApiClient, putDataReq);

        Wearable.DataApi.putDataItem(googleApiClient, putDataReq)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.e(LOG_TAG, "sendWeather ERROR: failed to putDataItem, status code: "
                                    + dataItemResult.getStatus().getStatusCode());
                        } else {
                            Log.i(LOG_TAG, "sendWeather SUCCESS: putDataItem, status code: "
                                    + dataItemResult.getStatus().getStatusCode());
                        }
                    }
                });
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        Log.i(LOG_TAG, "onDataChanged: " + dataEvents);

        for (DataEvent event : dataEvents) {
            Uri uri = event.getDataItem().getUri();
            String nodeId = uri.getHost();
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/weatherData") == 0) {
//                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.i(LOG_TAG, "onDataChanges /weatherData");

                    // Send the RPC
//                    Wearable.MessageApi.sendMessage(googleApiClient, nodeId,
//                            "/weatherData", uri.toString().getBytes());

                } else if (item.getUri().getPath().compareTo("/weatherIcon") == 0) {
//                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.i(LOG_TAG, "onDataChanges /weatherIcon");
//                    Wearable.MessageApi.sendMessage(googleApiClient, nodeId,
//                            "/weatherIcon", uri.toString().getBytes());
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.i(LOG_TAG, "onMessageReceived: " + messageEvent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(googleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void pause() {
        if (todayWeatherTask != null) {
            todayWeatherTask.cancel(true);
            todayWeatherTask = null;
        }
        Wearable.DataApi.removeListener(googleApiClient, this);
        googleApiClient.disconnect();
    }

    public void call() {
        createAndExecuteTask();
    }

    private void createAndExecuteTask() {
        todayWeatherTask = new TodayWeatherTask(this);
        todayWeatherTask.execute();
    }

    private void sendWeatherDataToWatch(DataMap config) {
//        Wearable.MessageApi.sendMessage(googleApiClient, "",
//                "/weatherData", config.toByteArray()).setResultCallback(
//                new ResultCallback<MessageApi.SendMessageResult>() {
//                    @Override
//                    public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
//                        if (!sendMessageResult.getStatus().isSuccess()) {
//                            // Failed to send message
//                        }
//                    }
//                }
//        );
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/weatherData");
        putDataMapReq.getDataMap().putString(TodayWeatherTask.KEY_WEATHER_LOW_TEMP, config.getString(TodayWeatherTask.KEY_WEATHER_LOW_TEMP));
        putDataMapReq.getDataMap().putString(TodayWeatherTask.KEY_WEATHER_MAX_TEMP, config.getString(TodayWeatherTask.KEY_WEATHER_MAX_TEMP));
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        // PendingResult<DataApi.DataItemResult> pendingResult =
//        PendingResult<DataApi.DataItemResult> dataItemResultPendingResult = Wearable.DataApi.putDataItem(googleApiClient, putDataReq);
//        dataItemResultPendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
//            @Override
//            public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
//                if (dataItemResult.getStatus().isSuccess()) {
//                    Log.d(LOG_TAG, "sendWeatherDataToWatch - Data item set: " + dataItemResult.getDataItem().getUri());
//                }
//            }
//        });
        Wearable.DataApi.putDataItem(googleApiClient, putDataReq)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.e(LOG_TAG, "sendWeatherDataToWatch ERROR: failed to putDataItem, status code: "
                                    + dataItemResult.getStatus().getStatusCode());
                        } else {
                            Log.i(LOG_TAG, "sendWeatherDataToWatch SUCCESS: putDataItem, status code: "
                                    + dataItemResult.getStatus().getStatusCode());
                        }
                    }
                });
    }

    private void sendWeatherIconToWatch(Bitmap bitmap) {
//        PutDataRequest request = PutDataRequest.create("/weatherIcon");
//        request.putAsset(TodayWeatherTask.KEY_WEATHER_ICON, Utility.createAssetFromBitmap(bitmap));
////        PendingResult<DataApi.DataItemResult> dataItemResultPendingResult = Wearable.DataApi.putDataItem(googleApiClient, request);
//        Wearable.DataApi.putDataItem(googleApiClient, request)
//                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
//                    @Override
//                    public void onResult(DataApi.DataItemResult dataItemResult) {
//                        if (!dataItemResult.getStatus().isSuccess()) {
//                            Log.e(LOG_TAG, "sendWeatherIconToWatch ERROR: failed to putDataItem, status code: "
//                                    + dataItemResult.getStatus().getStatusCode());
//                        } else {
//                            Log.i(LOG_TAG, "sendWeatherIconToWatch SUCCESS: putDataItem, status code: "
//                                    + dataItemResult.getStatus().getStatusCode());
//                        }
//                    }
//                });

        Asset asset = Utility.createAssetFromBitmap(bitmap);
        PutDataMapRequest dataMap = PutDataMapRequest.create("/weatherIcon");
        dataMap.getDataMap().putAsset(TodayWeatherTask.KEY_WEATHER_ICON, asset);
        PutDataRequest putDataRequest = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                .putDataItem(googleApiClient, putDataRequest);
    }
}
