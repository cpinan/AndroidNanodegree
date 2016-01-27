package com.carlospinan.builditbiggerandroidlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * @author Carlos Piñan
 */
public class JokeActivity extends AppCompatActivity {

    public static final String JOKE_KEY = "jokeResultKey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_joke);
        String jokeResult = getString(R.string.derp);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey(JOKE_KEY)) {
            jokeResult = intent.getExtras().getString(JOKE_KEY);
        }
        TextView resultJokeTextView = (TextView) findViewById(R.id.resultJokeTextView);
        resultJokeTextView.setText(jokeResult);
    }
}
