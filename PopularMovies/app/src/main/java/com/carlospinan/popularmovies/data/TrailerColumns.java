package com.carlospinan.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * @author Carlos Piñan
 */
public interface TrailerColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    public static final String _MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT)
    public static final String _TRAILER_ID = "trailer_id";

    @DataType(DataType.Type.TEXT)
    public static final String _ISO6391 = "iso6391";

    @DataType(DataType.Type.TEXT)
    public static final String _KEY = "key";

    @DataType(DataType.Type.TEXT)
    public static final String _NAME = "name";

    @DataType(DataType.Type.TEXT)
    public static final String _SITE = "site";

    @DataType(DataType.Type.INTEGER)
    public static final String _SIZE = "size";

    @DataType(DataType.Type.TEXT)
    public static final String _TYPE = "type";

}
