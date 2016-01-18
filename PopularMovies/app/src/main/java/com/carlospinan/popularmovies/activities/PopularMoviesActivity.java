package com.carlospinan.popularmovies.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.fragments.DetailPopularMovieFragment;
import com.carlospinan.popularmovies.fragments.PopularMoviesFragment;
import com.carlospinan.popularmovies.helpers.Globals;
import com.carlospinan.popularmovies.listeners.OnFragmentInteractionListener;
import com.carlospinan.popularmovies.models.MovieModel;

/**
 * @author Carlos Piñan
 */
public class PopularMoviesActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private MovieModel movieModel;
    private boolean isTwoPane;
    private String sortOrderOption;
    private MenuItem mostPopularMenuItem, highestRatedMenuItem;

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
        sortOrderOption = getString(R.string.optionMostPopular);
    }

    @Override
    public boolean isTwoPane() {
        return isTwoPane;
    }

    @Override
    public void showMovieDetail(MovieModel movieModel, String transitionNameId) {
        this.movieModel = movieModel;
        if (isTwoPane) {
            loadMovieDetail();
        }
    }

    @Override
    public void getFirstMovie(MovieModel movieModel) {
        if (isTwoPane && this.movieModel == null) {
            this.movieModel = movieModel;
            loadMovieDetail();
        }
    }

    @Override
    public String getSortOrder() {
        return sortOrderOption;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        mostPopularMenuItem = menu.findItem(R.id.mostPopularMenu);
        highestRatedMenuItem = menu.findItem(R.id.highestRatedMenu);
        manageMenuItemStates();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mostPopularMenu || item.getItemId() == R.id.highestRatedMenu) {
            if (item.getItemId() == R.id.mostPopularMenu) {
                sortOrderOption = getString(R.string.optionMostPopular);
            } else {
                sortOrderOption = getString(R.string.optionHighestRated);
            }
            manageMenuItemStates();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void manageMenuItemStates() {
        if (mostPopularMenuItem != null && highestRatedMenuItem != null) {
            if (sortOrderOption == null) {
                sortOrderOption = getString(R.string.optionMostPopular);
            }
            boolean isMostPopular = sortOrderOption.equalsIgnoreCase(getString(R.string.optionMostPopular));
            mostPopularMenuItem.setEnabled(!isMostPopular);
            highestRatedMenuItem.setEnabled(isMostPopular);
            PopularMoviesFragment fragment;
            if (isTwoPane) {
                fragment = (PopularMoviesFragment) getSupportFragmentManager().findFragmentById(R.id.popularMoviesFragment);
            } else {
                fragment = (PopularMoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

            }
            if (fragment != null) {
                fragment.clearAndDiscoverMovies();
            }
        }
    }

    private void loadMovieDetail() {
        DetailPopularMovieFragment detailPopularMovieFragment = (DetailPopularMovieFragment) getSupportFragmentManager().findFragmentById(R.id.detailPopularMoviesFragment);
        if (detailPopularMovieFragment != null) {
            detailPopularMovieFragment.updateMovieDetail(movieModel);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (movieModel != null) {
            outState.putSerializable(Globals.MOVIE_KEY, movieModel);
        }
        outState.putString(Globals.SORT_KEY, sortOrderOption);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            sortOrderOption = savedInstanceState.getString(Globals.SORT_KEY);
            if (savedInstanceState.containsKey(Globals.MOVIE_KEY)) {
                movieModel = (MovieModel) savedInstanceState.getSerializable(Globals.MOVIE_KEY);
                if (isTwoPane) {
                    loadMovieDetail();
                }
            }
            manageMenuItemStates();
        }
    }
}