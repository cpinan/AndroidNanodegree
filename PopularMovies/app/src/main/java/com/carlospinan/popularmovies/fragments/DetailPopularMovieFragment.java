package com.carlospinan.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.helpers.APIHelper;
import com.carlospinan.popularmovies.models.MovieModel;

/**
 * @author Carlos Piñan
 */
public class DetailPopularMovieFragment extends Fragment {

    public static final String MOVIE_KEY = "movieKey";

    private View view;
    private MovieModel movieModel;

    public static DetailPopularMovieFragment newInstance(MovieModel movieModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MOVIE_KEY, movieModel);
        DetailPopularMovieFragment mDetailPopularMovieFragment = new DetailPopularMovieFragment();
        mDetailPopularMovieFragment.setArguments(bundle);
        return mDetailPopularMovieFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(MOVIE_KEY)) {
            movieModel = (MovieModel) getArguments().getSerializable(MOVIE_KEY);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_popular_movie, container, false);
        updateMovieDetail(movieModel);
        return view;
    }

    public void updateMovieDetail(MovieModel movieModel) {
        TextView movieTitleTextView = (TextView) view.findViewById(R.id.movieTitleTextView);
        ImageView movieImageView = (ImageView) view.findViewById(R.id.movieImageView);
        if (movieModel != null) {
            movieTitleTextView.setText(movieModel.getTitle());
            if (movieModel.getPosterPath() != null) {
                String imagePath = APIHelper.get().getImagePath(APIHelper.IMAGE_SIZE.W500, movieModel.getPosterPath());
                APIHelper.get().loadImage(movieImageView, imagePath);
                movieImageView.setVisibility(View.VISIBLE);
            } else {
                movieImageView.setVisibility(View.GONE);
            }
        }
    }

}
