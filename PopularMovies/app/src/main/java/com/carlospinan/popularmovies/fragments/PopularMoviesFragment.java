package com.carlospinan.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.activities.DetailPopularMovieActivity;
import com.carlospinan.popularmovies.adapters.PopularMoviesAdapter;
import com.carlospinan.popularmovies.data.MovieProvider;
import com.carlospinan.popularmovies.helpers.APIHelper;
import com.carlospinan.popularmovies.helpers.DatabaseHelper;
import com.carlospinan.popularmovies.helpers.Globals;
import com.carlospinan.popularmovies.listeners.EndlessRecyclerOnScrollListener;
import com.carlospinan.popularmovies.listeners.OnFragmentInteractionListener;
import com.carlospinan.popularmovies.models.MovieModel;
import com.carlospinan.popularmovies.responses.DiscoverMoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * @author Carlos Piñan
 */
public class PopularMoviesFragment extends Fragment implements PopularMoviesAdapter.PopularMoviesListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;

    private int currentPage;
    private RecyclerView recyclerView;
    private OnFragmentInteractionListener listener;
    private PopularMoviesAdapter popularMoviesAdapter;
    private Call<DiscoverMoviesResponse> discoverMoviesResponseCall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentPage = 1;
        List<MovieModel> movieModelList = new ArrayList<>();
        if (savedInstanceState != null) {
            movieModelList = savedInstanceState.getParcelableArrayList(Globals.MOVIES_ELEMENT_KEY);
            currentPage = savedInstanceState.getInt(Globals.PAGE_KEY);
        }
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_popular_movies, container, false);
        popularMoviesAdapter = new PopularMoviesAdapter(getContext(), movieModelList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), !listener.isTwoPane() ? 2 : 3);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(popularMoviesAdapter);
        recyclerView.addOnScrollListener(
                new EndlessRecyclerOnScrollListener(gridLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        if (!listener.getSortOrder().equalsIgnoreCase(Globals.SORT_FAVORITES)) {
                            discoverMovies(++page);
                            currentPage = page;
                        }
                    }
                });
        popularMoviesAdapter.setListener(this);
        if (savedInstanceState == null) {
            discoverMovies(currentPage);
        } else {
            int scrollPosition = savedInstanceState.getInt(Globals.SCROLL_POSITION_KEY);
            int count = gridLayoutManager.getChildCount();
            if (scrollPosition != RecyclerView.NO_POSITION && scrollPosition < count) {
                gridLayoutManager.scrollToPosition(scrollPosition);
            }
        }
        return recyclerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Globals.PAGE_KEY, currentPage);
        outState.putParcelableArrayList(Globals.MOVIES_ELEMENT_KEY, (ArrayList<? extends Parcelable>) popularMoviesAdapter.getMovies());
        if (recyclerView != null) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            outState.putInt(Globals.SCROLL_POSITION_KEY, gridLayoutManager.findFirstCompletelyVisibleItemPosition());
        }
        super.onSaveInstanceState(outState);
    }

    private void discoverMovies(int page) {
        String sortOrder = listener.getSortOrder();
        if (!sortOrder.equalsIgnoreCase(Globals.SORT_FAVORITES)) {
            discoverMoviesResponseCall = APIHelper.get().getRetrofitService().getMovies(
                    page,
                    listener.getSortOrder()
            );
            discoverMoviesResponseCall.enqueue(new Callback<DiscoverMoviesResponse>() {
                @Override
                public void onResponse(Response<DiscoverMoviesResponse> response) {
                    List<MovieModel> movieModels = response.body().getResults();
                    if (movieModels != null && !movieModels.isEmpty()) {
                        popularMoviesAdapter.add(response.body().getResults());
                        listener.getFirstMovie(response.body().getResults().get(0));
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        }
    }

    @Override
    public void onMovieClicked(View view, MovieModel movieModel, String transitionNameId) {
        listener.showMovieDetail(movieModel, transitionNameId);
        if (!listener.isTwoPane()) {
            Intent intent = new Intent(getActivity(), DetailPopularMovieActivity.class);
            intent.putExtra(Globals.LOAD_FROM_DATABASE_KEY, listener.getSortOrder().equalsIgnoreCase(Globals.SORT_FAVORITES));
            intent.putExtra(Globals.MOVIE_KEY, movieModel);
            intent.putExtra(Globals.TRANSITION_IMAGE_KEY, transitionNameId);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(), view, transitionNameId);
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }
    }

    public void clearAndDiscoverMovies() {
        if (popularMoviesAdapter != null) {
            popularMoviesAdapter.clear();
            currentPage = 1;
            discoverMovies(currentPage);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieProvider.Movies.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<MovieModel> movieModels = DatabaseHelper.get().getMoviesFromCursor(data);
        popularMoviesAdapter.add(movieModels);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        popularMoviesAdapter.clear();
    }
}