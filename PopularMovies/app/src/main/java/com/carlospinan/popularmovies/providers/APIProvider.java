package com.carlospinan.popularmovies.providers;

import com.carlospinan.popularmovies.helpers.APIHelper;
import com.carlospinan.popularmovies.responses.DiscoverMoviesResponse;
import com.carlospinan.popularmovies.responses.ReviewsResponse;
import com.carlospinan.popularmovies.responses.TrailersResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * @author Carlos Piñan
 */
public interface APIProvider {

    @GET("discover/movie?api_key=" + APIHelper.API_KEY)
    Call<DiscoverMoviesResponse> getMovies(
            @Query("page") int page,
            @Query("sort_by") String sortBy
    );

    @GET("movie/{movieID}/videos?api_key=" + APIHelper.API_KEY)
    Call<TrailersResponse> getTrailers(
            @Path("movieID") int movieID,
            @Query("page") int page
    );

    @GET("movie/{movieID}/reviews?api_key=" + APIHelper.API_KEY)
    Call<ReviewsResponse> getReviews(
            @Path("movieID") int movieID,
            @Query("page") int page
    );
}
