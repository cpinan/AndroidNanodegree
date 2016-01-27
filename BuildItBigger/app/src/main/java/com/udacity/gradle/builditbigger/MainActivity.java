package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.carlospinan.builditbiggerandroidlibrary.JokeActivity;
import com.udacity.gradle.builditbigger.listeners.JokeListener;
import com.udacity.gradle.builditbigger.tasks.JokesAsyncTask;


public class MainActivity extends AppCompatActivity implements JokeListener {

    private ProgressBar progressBar;
    private JokesAsyncTask jokesAsyncTask;
    private MainActivityFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    public void tellJoke(View view) {
        if (fragment != null) {
            fragment.onCallFragment();
        }
        progressBar.setVisibility(View.VISIBLE);
        jokesAsyncTask = new JokesAsyncTask(this);
        jokesAsyncTask.execute();
//        String joke = MyJavaLibrary.getJoke();
//        Toast.makeText(this, joke, Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this, JokeActivity.class);
//        intent.putExtra(JokeActivity.JOKE_KEY, joke);
//        startActivity(intent);
    }

    @Override
    public void onResult(String joke) {
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, JokeActivity.class);
        intent.putExtra(JokeActivity.JOKE_KEY, joke);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (jokesAsyncTask != null && jokesAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            jokesAsyncTask.cancel(true);
            jokesAsyncTask = null;
        }
    }
}
