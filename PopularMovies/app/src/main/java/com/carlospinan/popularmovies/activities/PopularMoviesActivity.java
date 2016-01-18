package com.carlospinan.popularmovies.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.fragments.DetailPopularMovieFragment;
import com.carlospinan.popularmovies.fragments.PopularMoviesFragment;
import com.carlospinan.popularmovies.listeners.OnFragmentInteractionListener;
import com.carlospinan.popularmovies.models.MovieModel;

/**
 * @author Carlos Piñan
 */
public class PopularMoviesActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.fragmentContainer) != null) {
            isTwoPane = false;
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new PopularMoviesFragment());
                fragmentTransaction.commit();
            }
        } else {
            isTwoPane = true;
        }
    }

    @Override
    public boolean isTwoPane() {
        return isTwoPane;
    }

    @Override
    public void showMovieDetail(MovieModel movieModel) {
        DetailPopularMovieFragment detailPopularMovieFragment = (DetailPopularMovieFragment) getSupportFragmentManager().findFragmentById(R.id.detailPopularMoviesFragment);
        if (detailPopularMovieFragment != null) {
            detailPopularMovieFragment.updateMovieDetail(movieModel);
        }
    }

}