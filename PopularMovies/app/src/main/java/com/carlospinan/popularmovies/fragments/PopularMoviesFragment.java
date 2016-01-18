package com.carlospinan.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.activities.DetailPopularMovieActivity;
import com.carlospinan.popularmovies.adapters.PopularMoviesAdapter;
import com.carlospinan.popularmovies.helpers.APIHelper;
import com.carlospinan.popularmovies.listeners.EndlessRecyclerOnScrollListener;
import com.carlospinan.popularmovies.listeners.OnFragmentInteractionListener;
import com.carlospinan.popularmovies.models.MovieModel;
import com.carlospinan.popularmovies.responses.DiscoverMoviesResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * @author Carlos Piñan
 */
public class PopularMoviesFragment extends Fragment implements PopularMoviesAdapter.PopularMoviesListener {

    private int currentPage;
    private String sortOrderOption;
    private MenuItem mostPopularMenuItem, highestRatedMenuItem;
    private OnFragmentInteractionListener listener;
    private PopularMoviesAdapter popularMoviesAdapter;
    private Call<DiscoverMoviesResponse> discoverMoviesResponseCall;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentPage = 1;
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_popular_movies, container, false);
        popularMoviesAdapter = new PopularMoviesAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), listener.isTwoPane() ? 3 : 2);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(popularMoviesAdapter);
        recyclerView.addOnScrollListener(
                new EndlessRecyclerOnScrollListener(gridLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        discoverMovies(++page);
                        currentPage = page;
                    }
                });
        popularMoviesAdapter.setListener(this);
        sortOrderOption = getString(R.string.optionMostPopular);
        discoverMovies(currentPage);
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
    public void onPause() {
        super.onPause();
        if (discoverMoviesResponseCall != null) {
            discoverMoviesResponseCall.cancel();
        }
    }

    private void discoverMovies(int page) {
        discoverMoviesResponseCall = APIHelper.get().getRetrofitService().getMovies(
                page,
                sortOrderOption
        );
        discoverMoviesResponseCall.enqueue(new Callback<DiscoverMoviesResponse>() {
            @Override
            public void onResponse(Response<DiscoverMoviesResponse> response) {
                popularMoviesAdapter.add(response.body().getResults());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    public void onMovieClicked(MovieModel movieModel) {
        if (!listener.isTwoPane()) {
            Intent intent = new Intent(getActivity(), DetailPopularMovieActivity.class);
            intent.putExtra(DetailPopularMovieFragment.MOVIE_KEY, movieModel);
            startActivity(intent);
        } else {
            listener.showMovieDetail(movieModel);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        mostPopularMenuItem = menu.findItem(R.id.mostPopularMenu);
        highestRatedMenuItem = menu.findItem(R.id.highestRatedMenu);
        super.onCreateOptionsMenu(menu, inflater);
        manageMenuItemStates();
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
            if (popularMoviesAdapter != null) {
                popularMoviesAdapter.clear();
                currentPage = 1;
                discoverMovies(currentPage);
            }
        }
    }

}
