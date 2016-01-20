package com.carlospinan.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * @author Carlos Piñan
 */
@Database(version = MovieDatabase.VERSION)
public class MovieDatabase {

    private MovieDatabase() {
    }

    public static final int VERSION = 1;

    @Table(MovieColumns.class)
    public static final String MOVIES = "movies";

    @Table(TrailerColumns.class)
    public static final String TRAILERS = "trailers";

    @Table(ReviewColumns.class)
    public static final String REVIEWS = "reviews";
}
