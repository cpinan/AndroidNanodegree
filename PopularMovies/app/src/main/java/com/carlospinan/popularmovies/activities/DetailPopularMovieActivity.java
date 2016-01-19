package com.carlospinan.popularmovies.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.fragments.DetailPopularMovieFragment;
import com.carlospinan.popularmovies.helpers.APIHelper;
import com.carlospinan.popularmovies.helpers.Globals;
import com.carlospinan.popularmovies.models.MovieModel;

/**
 * @author Carlos Piñan
 */
public class DetailPopularMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_popular_movie);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        } else {
            if (savedInstanceState == null) {
                ImageView parallaxImageView = (ImageView) findViewById(R.id.parallaxImageView);
                MovieModel movieModel = getIntent().getParcelableExtra(Globals.MOVIE_KEY);
                String backdrop = movieModel.getBackdropPath();
                if (backdrop != null) {
                    backdrop = APIHelper.get().getImagePath(APIHelper.IMAGE_SIZE.W780, backdrop);
                    APIHelper.get().loadImage(parallaxImageView, backdrop);
                } else {
                    parallaxImageView.setVisibility(View.GONE);
                }
                String transitionIdName = getIntent().getExtras().getString(Globals.TRANSITION_IMAGE_KEY);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setTitle(R.string.movie_detail);
                DetailPopularMovieFragment fragment = DetailPopularMovieFragment.newInstance(movieModel, transitionIdName);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
