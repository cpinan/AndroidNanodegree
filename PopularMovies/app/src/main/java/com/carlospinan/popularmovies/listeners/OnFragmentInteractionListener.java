package com.carlospinan.popularmovies.listeners;

import com.carlospinan.popularmovies.models.MovieModel;

/**
 * @author Carlos Piñan
 */
public interface OnFragmentInteractionListener {

    boolean isTwoPane();

    void showMovieDetail(MovieModel movieModel, String transitionNameId);

    void getFirstMovie(MovieModel movieModel);

    String getSortOrder();
}
