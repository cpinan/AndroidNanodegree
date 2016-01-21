package com.carlospinan.popularmovies.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carlospinan.popularmovies.R;
import com.carlospinan.popularmovies.helpers.APIHelper;
import com.carlospinan.popularmovies.helpers.DatabaseHelper;
import com.carlospinan.popularmovies.helpers.Globals;
import com.carlospinan.popularmovies.models.MovieModel;
import com.carlospinan.popularmovies.models.ReviewModel;
import com.carlospinan.popularmovies.models.TrailerModel;
import com.carlospinan.popularmovies.responses.ReviewsResponse;
import com.carlospinan.popularmovies.responses.TrailersResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * @author Carlos Piñan
 */
public class DetailPopularMovieFragment extends Fragment {

    @Bind(R.id.movieTitleTextView)
    TextView movieTitleTextView;

    @Bind(R.id.movieImageView)
    ImageView movieImageView;

    @Bind(R.id.releaseDateTextView)
    TextView releaseDateTextView;

    @Bind(R.id.ratingTextView)
    TextView ratingTextView;

    @Bind(R.id.synopsisTextView)
    TextView synopsisTextView;

    @Bind(R.id.favoriteFloatingButton)
    FloatingActionButton favoriteFloatingButton;

    @Bind(R.id.trailersContainer)
    LinearLayout trailersContainer;

    @Bind(R.id.reviewsContainer)
    LinearLayout reviewsContainer;

    private List<ReviewModel> reviewModelList;
    private List<TrailerModel> trailerModelList;

    private boolean loadFromDatabase;
    private String transitionImageName;
    private ShareActionProvider mShareActionProvider;

    private Call<TrailersResponse> trailersResponseCall;
    private Call<ReviewsResponse> reviewsResponseCall;

    public static DetailPopularMovieFragment newInstance(MovieModel movieModel, String transitionImageName, boolean loadFromDatabase) {
        DetailPopularMovieFragment mDetailPopularMovieFragment = new DetailPopularMovieFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Globals.MOVIE_KEY, movieModel);
        bundle.putString(Globals.TRANSITION_IMAGE_KEY, transitionImageName);
        bundle.putBoolean(Globals.LOAD_FROM_DATABASE_KEY, loadFromDatabase);
        mDetailPopularMovieFragment.setArguments(bundle);
        return mDetailPopularMovieFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trailerModelList = new ArrayList<>();
        reviewModelList = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_popular_movie, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void updateMovieDetail(final MovieModel movieModel, boolean loadFromDatabase) {
        this.loadFromDatabase = loadFromDatabase;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            movieImageView.setTransitionName(transitionImageName);
        }
        if (movieModel != null) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                movieTitleTextView.setVisibility(View.VISIBLE);
            } else {
                movieTitleTextView.setVisibility(View.GONE);
            }

            reviewsContainer.removeAllViews();
            trailersContainer.removeAllViews();

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

            favoriteFloatingButton.setEnabled(true);
            favoriteFloatingButton.setImageResource(android.R.drawable.star_big_off);

            // Database validation.
            if (DatabaseHelper.get().isMovieSavedOnFavorites(
                    getActivity(),
                    movieModel.getId()
            )) {
                favoriteFloatingButton.setEnabled(false);
                favoriteFloatingButton.setImageResource(android.R.drawable.star_big_on);
            } else {
                favoriteFloatingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (movieModel != null) {
                            DatabaseHelper.get().insertMovie(
                                    getActivity(),
                                    movieModel,
                                    trailerModelList,
                                    reviewModelList
                            );
                            favoriteFloatingButton.setEnabled(false);
                            favoriteFloatingButton.setImageResource(android.R.drawable.star_big_on);
                        }
                    }
                });
            }
            requestTrailersAndComments(movieModel.getId());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null && args.containsKey(Globals.MOVIE_KEY)) {
            transitionImageName = args.getString(Globals.TRANSITION_IMAGE_KEY);
            MovieModel movieModel = args.getParcelable(Globals.MOVIE_KEY);
            boolean loadFromDatabase = args.getBoolean(Globals.LOAD_FROM_DATABASE_KEY);
            updateMovieDetail(movieModel, loadFromDatabase);
        }
    }

    @Override
    public void onPause() {
        if (trailersResponseCall != null) {
            trailersResponseCall.cancel();
        }
        Glide.with(getContext()).pauseRequests();
        super.onPause();
    }

    private void requestTrailersAndComments(int movieId) {
        if (!loadFromDatabase) {
            trailersResponseCall = APIHelper.get().getRetrofitService().getTrailers(movieId);
            trailersResponseCall.enqueue(new Callback<TrailersResponse>() {
                @Override
                public void onResponse(Response<TrailersResponse> response) {
                    if (response != null && response.body() != null) {
                        trailerModelList = response.body().getResults();
                        if (!trailerModelList.isEmpty()) {
                            fillTrailers();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) { /* UNUSED */ }
            });

            reviewsResponseCall = APIHelper.get().getRetrofitService().getReviews(movieId);
            reviewsResponseCall.enqueue(new Callback<ReviewsResponse>() {
                @Override
                public void onResponse(Response<ReviewsResponse> response) {
                    if (response != null && response.body() != null) {
                        reviewModelList = response.body().getResults();
                        if (!reviewModelList.isEmpty()) {
                            fillReviews();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) { /* UNUSED */ }
            });

        } else {
            reviewModelList = DatabaseHelper.get().getReviewsFromDatabase(getActivity(), movieId);
            trailerModelList = DatabaseHelper.get().getTrailersFromDatabase(getActivity(), movieId);
            fillTrailers();
            fillReviews();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.shareMenu);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private Intent getShareIntent() {
        if (!trailerModelList.isEmpty()) {
            TrailerModel trailerModel = trailerModelList.get(0);
            String url = APIHelper.get().getVideoPath(trailerModel.getSite(), trailerModel.getKey());
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
            return shareIntent;
        }
        return null;
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(getShareIntent());
            getActivity().invalidateOptionsMenu();
        }
    }

    private void fillTrailers() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final TrailerModel trailerModel : trailerModelList) {
            View view = inflater.inflate(R.layout.list_trailers, null, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    APIHelper.get().loadVideo(getActivity(), trailerModel.getSite(), trailerModel.getKey());
                }
            });
            ((TextView) view.findViewById(R.id.trailerTextView)).setText(trailerModel.getName());
            trailersContainer.addView(view);
        }
        setShareIntent();
    }

    private void fillReviews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final ReviewModel reviewModel : reviewModelList) {
            View view = inflater.inflate(R.layout.list_reviews, null, false);
            TextView authorTextView = (TextView) view.findViewById(R.id.authorTextView);
            TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
            TextView urlTextView = (TextView) view.findViewById(R.id.urlTextView);
            authorTextView.setText(reviewModel.getAuthor());
            contentTextView.setText(Html.fromHtml(reviewModel.getContent()));
            if (reviewModel.getUrl() != null) {
                urlTextView.setText(reviewModel.getUrl());
                urlTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(reviewModel.getUrl()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        startActivity(intent);
                    }
                });
            }
            reviewsContainer.addView(view);
        }
    }
}
