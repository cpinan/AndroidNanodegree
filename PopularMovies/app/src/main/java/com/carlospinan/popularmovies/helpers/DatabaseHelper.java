package com.carlospinan.popularmovies.helpers;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.RemoteException;

import com.carlospinan.popularmovies.data.MovieColumns;
import com.carlospinan.popularmovies.data.MovieProvider;
import com.carlospinan.popularmovies.data.ReviewColumns;
import com.carlospinan.popularmovies.data.TrailerColumns;
import com.carlospinan.popularmovies.models.MovieModel;
import com.carlospinan.popularmovies.models.ReviewModel;
import com.carlospinan.popularmovies.models.TrailerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Piñan
 */
public class DatabaseHelper {

    private static DatabaseHelper instance;
    private static final String POPULAR_MOVIES_STORAGE = "a0fmas21e212e*afa";
    private static final String MOVIE_STORAGE_KEY = "movieStorageKey_%d";

    private DatabaseHelper() {

    }

    public static DatabaseHelper get() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public void insertMovie(
            Activity activity,
            MovieModel movieModel,
            List<TrailerModel> trailerModels,
            List<ReviewModel> reviewModels
    ) {
        // Trailers
        if (!trailerModels.isEmpty()) {
            ArrayList<ContentProviderOperation> trailerBatchOperation = new ArrayList<>(trailerModels.size());
            for (TrailerModel trailerModel : trailerModels) {
                ContentProviderOperation.Builder trailerBuilder = ContentProviderOperation.newInsert(
                        MovieProvider.Trailers.CONTENT_URI
                );
                trailerBuilder.withValue(TrailerColumns._MOVIE_ID, movieModel.getId());
                trailerBuilder.withValue(TrailerColumns._TRAILER_ID, trailerModel.getId());
                trailerBuilder.withValue(TrailerColumns._ISO6391, trailerModel.getIso6391());
                trailerBuilder.withValue(TrailerColumns._NAME, trailerModel.getName());
                trailerBuilder.withValue(TrailerColumns._KEY, trailerModel.getKey());
                trailerBuilder.withValue(TrailerColumns._SITE, trailerModel.getSite());
                trailerBuilder.withValue(TrailerColumns._SIZE, trailerModel.getSize());
                trailerBuilder.withValue(TrailerColumns._TYPE, trailerModel.getType());
                trailerBatchOperation.add(trailerBuilder.build());
            }
            try {
                activity.getContentResolver().applyBatch(MovieProvider.AUTHORITY, trailerBatchOperation);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        }

        // Reviews
        if (!reviewModels.isEmpty()) {
            ArrayList<ContentProviderOperation> reviewBatchOperation = new ArrayList<>(reviewModels.size());
            for (ReviewModel reviewModel : reviewModels) {
                ContentProviderOperation.Builder reviewBuilder = ContentProviderOperation.newInsert(
                        MovieProvider.Reviews.CONTENT_URI
                );
                reviewBuilder.withValue(ReviewColumns._MOVIE_ID, movieModel.getId());
                reviewBuilder.withValue(ReviewColumns._REVIEW_ID, reviewModel.getId());
                reviewBuilder.withValue(ReviewColumns._AUTHOR, reviewModel.getAuthor());
                reviewBuilder.withValue(ReviewColumns._CONTENT, reviewModel.getContent());
                reviewBuilder.withValue(ReviewColumns._URL, reviewModel.getUrl());
                reviewBatchOperation.add(reviewBuilder.build());
            }
            try {
                activity.getContentResolver().applyBatch(MovieProvider.AUTHORITY, reviewBatchOperation);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        }

        // Movie
        ContentValues movieContentValues = new ContentValues();
        movieContentValues.put(MovieColumns._ID, movieModel.getId());
        movieContentValues.put(MovieColumns._ADULT, movieModel.isAdult());
        movieContentValues.put(MovieColumns._BACKDROP_PATH, movieModel.getBackdropPath());
//        movieContentValues.put(MovieColumns._GENRES, movieModel.getGenreIds().toString());
        movieContentValues.put(MovieColumns._ORIGINAL_LANGUAGE, movieModel.getOriginalLanguage());
        movieContentValues.put(MovieColumns._ORIGINAL_TITLE, movieModel.getOriginalTitle());
        movieContentValues.put(MovieColumns._OVERVIEW, movieModel.getOverview());
        movieContentValues.put(MovieColumns._RELEASE_DATE, movieModel.getReleaseDate());
        movieContentValues.put(MovieColumns._POSTER_PATH, movieModel.getPosterPath());
        movieContentValues.put(MovieColumns._POPULARITY, movieModel.getPopularity());
        movieContentValues.put(MovieColumns._TITLE, movieModel.getTitle());
        movieContentValues.put(MovieColumns._VIDEO, movieModel.isVideo());
        movieContentValues.put(MovieColumns._VOTE_AVERAGE, movieModel.getVoteAverage());
        movieContentValues.put(MovieColumns._VOTE_COUNT, movieModel.getVoteCount());
        activity.getContentResolver().insert(MovieProvider.Movies.CONTENT_URI, movieContentValues);

        SharedPreferences sharedPreferences = activity.getSharedPreferences(POPULAR_MOVIES_STORAGE, Context.MODE_PRIVATE);

        int movieId = movieModel.getId();
        String movieKey = String.format(MOVIE_STORAGE_KEY, movieId);
        sharedPreferences.edit().putInt(movieKey, movieModel.getId()).commit();
    }

    public boolean isMovieSavedOnFavorites(
            Activity activity,
            int movieId
    ) {
        String movieKey = String.format(MOVIE_STORAGE_KEY, movieId);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(POPULAR_MOVIES_STORAGE, Context.MODE_PRIVATE);
        if (sharedPreferences.getInt(movieKey, -1) > 0) {
            return true;
        }
        return false;
    }

    public List<TrailerModel> getTrailersFromDatabase(Activity activity, int movieId) {
        List<TrailerModel> trailerModels = new ArrayList<>();
        Cursor trailerCursor = activity.getContentResolver().query(
                MovieProvider.Trailers.withId(movieId),
                null,
                null,
                null,
                null
        );
        while (trailerCursor.moveToNext()) {
            int trailerIdIndex = trailerCursor.getColumnIndex(TrailerColumns._TRAILER_ID);
            int iso6391Index = trailerCursor.getColumnIndex(TrailerColumns._ISO6391);
            int keyIndex = trailerCursor.getColumnIndex(TrailerColumns._KEY);
            int nameIndex = trailerCursor.getColumnIndex(TrailerColumns._NAME);
            int siteIndex = trailerCursor.getColumnIndex(TrailerColumns._SITE);
            int sizeIndex = trailerCursor.getColumnIndex(TrailerColumns._SIZE);
            int typeIndex = trailerCursor.getColumnIndex(TrailerColumns._TYPE);

            String trailerId = trailerCursor.getString(trailerIdIndex);
            String iso6391 = trailerCursor.getString(iso6391Index);
            String key = trailerCursor.getString(keyIndex);
            String name = trailerCursor.getString(nameIndex);
            String site = trailerCursor.getString(siteIndex);
            int size = trailerCursor.getInt(sizeIndex);
            String type = trailerCursor.getString(typeIndex);

            TrailerModel trailerModel = new TrailerModel();
            trailerModel.setId(trailerId);
            trailerModel.setIso6391(iso6391);
            trailerModel.setKey(key);
            trailerModel.setName(name);
            trailerModel.setSite(site);
            trailerModel.setSize(size);
            trailerModel.setType(type);
            trailerModels.add(trailerModel);

        }
        return trailerModels;
    }


    public List<ReviewModel> getReviewsFromDatabase(Activity activity, int movieId) {
        List<ReviewModel> reviewModels = new ArrayList<>();
        Cursor reviewsCursor = activity.getContentResolver().query(
                MovieProvider.Reviews.withId(movieId),
                null,
                null,
                null,
                null
        );
        while (reviewsCursor.moveToNext()) {
            int reviewIdIndex = reviewsCursor.getColumnIndex(ReviewColumns._REVIEW_ID);
            int authorIndex = reviewsCursor.getColumnIndex(ReviewColumns._AUTHOR);
            int contentIndex = reviewsCursor.getColumnIndex(ReviewColumns._CONTENT);
            int urlIndex = reviewsCursor.getColumnIndex(ReviewColumns._URL);

            String review = reviewsCursor.getString(reviewIdIndex);
            String author = reviewsCursor.getString(authorIndex);
            String content = reviewsCursor.getString(contentIndex);
            String url = reviewsCursor.getString(urlIndex);

            ReviewModel reviewModel = new ReviewModel();
            reviewModel.setId(review);
            reviewModel.setAuthor(author);
            reviewModel.setContent(content);
            reviewModel.setUrl(url);
            reviewModels.add(reviewModel);

        }
        return reviewModels;
    }

    public List<MovieModel> getMoviesFromCursor(Cursor movieCursor) {
        List<MovieModel> movieModels = new ArrayList<>();
        while (movieCursor.moveToNext()) {
            int originalTitleColumnIndex = movieCursor.getColumnIndex(MovieColumns._ORIGINAL_TITLE);
            int idIndex = movieCursor.getColumnIndex(MovieColumns._ID);
            int posterPathIndex = movieCursor.getColumnIndex(MovieColumns._POSTER_PATH);
            int overviewIndex = movieCursor.getColumnIndex(MovieColumns._OVERVIEW);
            int backdropPathIndex = movieCursor.getColumnIndex(MovieColumns._BACKDROP_PATH);
            int voteAverageIndex = movieCursor.getColumnIndex(MovieColumns._VOTE_AVERAGE);
            int releaseDateIndex = movieCursor.getColumnIndex(MovieColumns._RELEASE_DATE);

            String originalTitle = movieCursor.getString(originalTitleColumnIndex);
            int id = movieCursor.getInt(idIndex);
            String posterPath = movieCursor.getString(posterPathIndex);
            String overview = movieCursor.getString(overviewIndex);
            String backdropPath = movieCursor.getString(backdropPathIndex);
            double voteAverage = movieCursor.getDouble(voteAverageIndex);
            String releaseDate = movieCursor.getString(releaseDateIndex);

            MovieModel movieModel = new MovieModel();
            movieModel.setId(id);
            movieModel.setPosterPath(posterPath);
            movieModel.setOriginalTitle(originalTitle);
            movieModel.setOverview(overview);
            movieModel.setBackdropPath(backdropPath);
            movieModel.setVoteAverage(voteAverage);
            movieModel.setReleaseDate(releaseDate);
            movieModels.add(movieModel);
        }
        return movieModels;
    }

    public List<MovieModel> getMoviesFromDatabase(Activity activity) {
        Cursor movieCursor = activity.getContentResolver().query(
                MovieProvider.Movies.CONTENT_URI,
                null,
                null,
                null,
                null,
                null
        );
        return getMoviesFromCursor(movieCursor);
    }

}
