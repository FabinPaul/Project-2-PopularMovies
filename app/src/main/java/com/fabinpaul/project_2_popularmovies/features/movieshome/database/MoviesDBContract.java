package com.fabinpaul.project_2_popularmovies.features.movieshome.database;

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
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";
        public static final String COLUMN_MOVIE_VOTE_COUNT = "movie_vote_count";


    }
}
