package com.udacity.gradle.builditbigger.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.carlospinan.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.listeners.JokeListener;

import java.io.IOException;

/**
 * @author Carlos Piñan
 */
public class JokesAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String LOG_TAG = JokesAsyncTask.class.getSimpleName();

    private static MyApi myApiService;

    private JokeListener listener;

    public JokesAsyncTask(JokeListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (myApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(),
                    null)
                    .setRootUrl("https://just-circle-776.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {

                        }
                    });
            myApiService = builder.build();
        }
        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i(LOG_TAG, s);
        if (listener != null) {
            listener.onResult(s);
        }
    }
}
