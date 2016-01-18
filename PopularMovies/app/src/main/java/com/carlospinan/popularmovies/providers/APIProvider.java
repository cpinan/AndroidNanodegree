package com.carlospinan.popularmovies.providers;

import com.carlospinan.popularmovies.helpers.APIHelper;
import com.carlospinan.popularmovies.responses.DiscoverMoviesResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author Carlos Piñan
 */
public interface APIProvider {

    @GET("discover/movie?api_key="+ APIHelper.API_KEY)
    Call<DiscoverMoviesResponse> getMovies(
            @Query("page") int page,
            @Query("sort_by") String sortBy
    );

    /*
    @GET("discover/movie?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<MovieResponse> discoverMovies(@Query("page") int page, @Query("sort_by") Sort sort);

    @GET("genre/movie/list?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<GenreResponse> getMovieGenres();

    @GET("movie/{movieId}/videos?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<VideoResponse> getMovieVideos(@Path("movieId") long movieId);

    @GET("movie/{movieId}/reviews?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<ReviewResponse> getMovieReviews(@Path("movieId") long movieId);
     */
}
