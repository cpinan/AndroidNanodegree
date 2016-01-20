package com.carlospinan.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * @author Carlos Piñan
 */
public interface ReviewColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    public static final String _MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT)
    public static final String _REVIEW_ID = "review_id";

    @DataType(DataType.Type.TEXT)
    public static final String _AUTHOR = "author";

    @DataType(DataType.Type.TEXT)
    public static final String _CONTENT = "content";

    @DataType(DataType.Type.TEXT)
    public static final String _URL = "url";

}
