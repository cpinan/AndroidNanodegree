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
    @GET("discover/movie?api_key=" + APIHelper.API_KEY)
    Call<DiscoverMoviesResponse> getMovies(
            @Query("page") int page,
            @Query("sort_by") String sortBy
    );
}
