package com.carlospinan.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.activities.DetailPopularMovieActivity;
import com.carlospinan.popularmovies.adapters.PopularMoviesAdapter;
import com.carlospinan.popularmovies.helpers.APIHelper;
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
public class PopularMoviesFragment extends Fragment implements PopularMoviesAdapter.PopularMoviesListener {

    private int currentPage = 1;
    private OnFragmentInteractionListener listener;
    private PopularMoviesAdapter popularMoviesAdapter;
    private Call<DiscoverMoviesResponse> discoverMoviesResponseCall;
    private RecyclerView recyclerView;
    private List<MovieModel> movieModelList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentPage = 1;
        movieModelList = new ArrayList<>();
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
                        discoverMovies(++page);
                        currentPage = page;
                    }
                });
        popularMoviesAdapter.setListener(this);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Globals.PAGE_KEY, currentPage);
        outState.putParcelableArrayList(Globals.MOVIES_ELEMENT_KEY, (ArrayList<? extends Parcelable>) movieModelList);
        super.onSaveInstanceState(outState);
    }

    private void discoverMovies(int page) {
        discoverMoviesResponseCall = APIHelper.get().getRetrofitService().getMovies(
                page,
                listener.getSortOrder()
        );
        discoverMoviesResponseCall.enqueue(new Callback<DiscoverMoviesResponse>() {
            @Override
            public void onResponse(Response<DiscoverMoviesResponse> response) {
                popularMoviesAdapter.add(response.body().getResults());
                listener.getFirstMovie(response.body().getResults().get(0));
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    public void onMovieClicked(View view, MovieModel movieModel, String transitionNameId) {
        listener.showMovieDetail(movieModel, transitionNameId);
        if (!listener.isTwoPane()) {
            Intent intent = new Intent(getActivity(), DetailPopularMovieActivity.class);
            intent.putExtra(Globals.MOVIE_KEY, movieModel);
            intent.putExtra(Globals.TRANSITION_IMAGE_KEY, transitionNameId);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(), view, transitionNameId);
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }
    }

    public void clearAndDiscoverMovies() {
        if (popularMoviesAdapter != null) {
            pauseRequest();
            popularMoviesAdapter.clear();
            currentPage = 1;
            discoverMovies(currentPage);
        }
    }

    private void pauseRequest() {
        if (discoverMoviesResponseCall != null) {
            discoverMoviesResponseCall.cancel();
        }
    }

}
