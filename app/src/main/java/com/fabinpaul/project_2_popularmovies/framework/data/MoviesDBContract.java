package com.fabinpaul.project_2_popularmovies.framework.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 11:25 AM.
 */

@SuppressWarnings("WeakerAccess")
public final class MoviesDBContract {

    public static final String CONTENT_AUTHORITY = "com.fabinpaul.project_2_popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final String PATH_FAV_MOVIES = "fav_movies";

    private MoviesDBContract() {
    }

    public static final class MoviesTB implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_TYPE_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

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

        public static Uri buildMovieUri(int movieId) {
            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }

        public static int getMovieIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }

    public static final class FavouriteMoviesTB implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIES;

        public static final String CONTENT_TYPE_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIES;

        public static final String TABLE_NAME = "fav_movie_tb";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static Uri buildFavMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavMoveListUri() {
            return Uri.withAppendedPath(CONTENT_URI, PATH_MOVIES);
        }

        public static int getFavIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }
}
