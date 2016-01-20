package com.carlospinan.popularmovies.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * @author Carlos Piñan
 */
public interface MovieColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    public static final String _ADULT = "adult";

    @DataType(DataType.Type.TEXT)
    public static final String _BACKDROP_PATH = "backdrop_path";

    @DataType(DataType.Type.TEXT)
    public static final String _GENRES = "genre_ids";

    @DataType(DataType.Type.TEXT)
    public static final String _ORIGINAL_LANGUAGE = "original_language";

    @DataType(DataType.Type.TEXT)
    public static final String _ORIGINAL_TITLE = "original_title";

    @DataType(DataType.Type.TEXT)
    public static final String _OVERVIEW = "overview";

    @DataType(DataType.Type.TEXT)
    public static final String _RELEASE_DATE = "release_date";

    @DataType(DataType.Type.TEXT)
    public static final String _POSTER_PATH = "poster_path";

    @DataType(DataType.Type.REAL)
    public static final String _POPULARITY = "popularity";

    @DataType(DataType.Type.TEXT)
    public static final String _TITLE = "title";

    @DataType(DataType.Type.INTEGER)
    public static final String _VIDEO = "video";

    @DataType(DataType.Type.REAL)
    public static final String _VOTE_AVERAGE = "vote_average";

    @DataType(DataType.Type.INTEGER)
    public static final String _VOTE_COUNT = "vote_count";
}
