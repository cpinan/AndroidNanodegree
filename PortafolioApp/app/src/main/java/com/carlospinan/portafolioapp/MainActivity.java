package com.carlospinan.portafolioapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.spotifyStreamerButton).setOnClickListener(this);
        findViewById(R.id.scoresApp).setOnClickListener(this);
        findViewById(R.id.libraryApp).setOnClickListener(this);
        findViewById(R.id.buildItBigger).setOnClickListener(this);
        findViewById(R.id.xyzReader).setOnClickListener(this);
        findViewById(R.id.capstone).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Button button = (Button) v;
            String label = button.getText().toString();
            String message = String.format(getString(R.string.toastMessage), label);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }
}