package com.carlospinan.popularmovies.listeners;

import com.carlospinan.popularmovies.models.MovieModel;

/**
 * @author Carlos Piñan
 */
public interface OnFragmentInteractionListener {

    boolean isTwoPane();

    void showMovieDetail(MovieModel movieModel);
}
