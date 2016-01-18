package com.carlospinan.popularmovies.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.fragments.DetailPopularMovieFragment;
import com.carlospinan.popularmovies.models.MovieModel;

/**
 * @author Carlos Piñan
 */
public class DetailPopularMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        setContentView(R.layout.activity_detail_popular_movie);
        MovieModel movieModel = (MovieModel) getIntent().getExtras().getSerializable(DetailPopularMovieFragment.MOVIE_KEY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(movieModel.getTitle());

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, DetailPopularMovieFragment.newInstance(movieModel));
            fragmentTransaction.commit();
        }
    }
}
