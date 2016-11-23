package com.fabinpaul.project_2_popularmovies.framework.database;

import android.provider.BaseColumns;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 11:25 AM.
 */

public final class MoviesDBContract {

    private MoviesDBContract(){

    }

    public final class MoviesTB implements BaseColumns {

        public static final String TABLE_NAME = "movies_tb";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "movie_title";
        public static final String COLUMN_OVERVIEW = "movie_overview";
        public static final String COLUMN_VOTE_COUNT = "movie_vote_count";
        public static final String COLUMN_RELEASE_DATE = "movie_release_date";
        public static final String COLUMN_LANGUAGE = "movie_language";
        public static final String COLUMN_POSTER = "movie_poster";
        public static final String COLUMN_BACKGROUND = "movie_background";
        public static final String COLUMN_POPULARITY = "movie_popularity";
        public static final String COLUMN_VOTE_AVERAGE = "movie_vote_avg";
    }

    public final class FavouriteMoviesTB implements BaseColumns{
        public static final String TABLE_NAME = "fav_movie_tb";

        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}
