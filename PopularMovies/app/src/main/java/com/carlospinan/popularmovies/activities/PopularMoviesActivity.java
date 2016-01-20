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

    private boolean isTwoPane;
    private MovieModel movieModel;
    private String sortOrderOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTwoPane = true;
        sortOrderOption = Globals.SORT_MOST_POPULAR;
        if (savedInstanceState != null) {
            sortOrderOption = savedInstanceState.getString(Globals.SORT_KEY);
        }
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
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Globals.MOVIE_KEY)) {
                movieModel = savedInstanceState.getParcelable(Globals.MOVIE_KEY);
                if (isTwoPane) {
                    loadMovieDetail();
                }
            }
            clearAndDiscoverMovies();
        }
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
        return sortOrderOption == null ? Globals.SORT_MOST_POPULAR : sortOrderOption;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mostPopularMenu) {
            sortOrderOption = Globals.SORT_MOST_POPULAR;
        } else if (item.getItemId() == R.id.highestRatedMenu) {
            sortOrderOption = Globals.SORT_HIGHEST_RATED;
        } else if (item.getItemId() == R.id.favoriteMenu) {
            sortOrderOption = Globals.SORT_FAVORITES;
        } else {
            return super.onOptionsItemSelected(item);
        }
        clearAndDiscoverMovies();
        return true;
    }

    private void clearAndDiscoverMovies() {
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

    private void loadMovieDetail() {
        DetailPopularMovieFragment detailPopularMovieFragment = (DetailPopularMovieFragment) getSupportFragmentManager().findFragmentById(R.id.detailPopularMoviesFragment);
        if (detailPopularMovieFragment != null) {
            detailPopularMovieFragment.updateMovieDetail(movieModel, sortOrderOption.equalsIgnoreCase(Globals.SORT_FAVORITES));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (movieModel != null) {
            outState.putParcelable(Globals.MOVIE_KEY, movieModel);
        }
        outState.putString(Globals.SORT_KEY, sortOrderOption);
        super.onSaveInstanceState(outState);
    }

}