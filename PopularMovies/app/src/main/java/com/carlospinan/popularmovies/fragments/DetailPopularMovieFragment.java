package com.carlospinan.popularmovies.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.helpers.APIHelper;
import com.carlospinan.popularmovies.helpers.Globals;
import com.carlospinan.popularmovies.models.MovieModel;

/**
 * @author Carlos Piñan
 */
public class DetailPopularMovieFragment extends Fragment {

    private TextView movieTitleTextView;
    private ImageView movieImageView;
    private TextView releaseDateTextView;
    private TextView ratingTextView;
    private TextView synopsisTextView;

    private String transitionImageName;

    public static DetailPopularMovieFragment newInstance(MovieModel movieModel, String transitionImageName) {
        DetailPopularMovieFragment mDetailPopularMovieFragment = new DetailPopularMovieFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Globals.MOVIE_KEY, movieModel);
        bundle.putString(Globals.TRANSITION_IMAGE_KEY, transitionImageName);
        mDetailPopularMovieFragment.setArguments(bundle);
        return mDetailPopularMovieFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_popular_movie, container, false);
        movieTitleTextView = (TextView) view.findViewById(R.id.movieTitleTextView);
        movieImageView = (ImageView) view.findViewById(R.id.movieImageView);
        releaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);
        ratingTextView = (TextView) view.findViewById(R.id.ratingTextView);
        synopsisTextView = (TextView) view.findViewById(R.id.synopsisTextView);
        return view;
    }

    public void updateMovieDetail(MovieModel movieModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            movieImageView.setTransitionName(transitionImageName);
        }
        if (movieModel != null) {
            movieTitleTextView.setText(movieModel.getOriginalTitle());
            releaseDateTextView.setText(movieModel.getReleaseDate());
            ratingTextView.setText(String.format("%.2f / 10", movieModel.getVoteAverage()));
            synopsisTextView.setText(movieModel.getOverview());
            if (movieModel.getPosterPath() != null) {
                String imagePath = APIHelper.get().getImagePath(movieModel.getPosterPath());
                APIHelper.get().loadImage(movieImageView, imagePath);
                movieImageView.setVisibility(View.VISIBLE);
            } else {
                movieImageView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null && args.containsKey(Globals.MOVIE_KEY)) {
            transitionImageName = args.getString(Globals.TRANSITION_IMAGE_KEY);
            MovieModel movieModel = (MovieModel) args.getSerializable(Globals.MOVIE_KEY);
            updateMovieDetail(movieModel);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.with(getContext()).pauseRequests();
    }

}
