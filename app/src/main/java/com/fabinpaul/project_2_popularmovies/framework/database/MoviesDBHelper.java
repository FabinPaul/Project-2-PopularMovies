package com.fabinpaul.project_2_popularmovies.framework.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 10:16 AM.
 */


/**
  CREATE TABLE fav_movie_tb (_id INTEGER PRIMARY KEY AUTOINCREMENT, movie_id INTEGER NOT NULL, UNIQUE ( movie_id ) ON CONFLICT REPLACE FOREIGN KEY ( movie_id ) REFERENCES movies_tb ( movie_id ))

 CREATE TABLE movies_tb (_id INTEGER PRIMARY KEY AUTOINCREMENT, movie_id INTEGER NOT NULL, movie_title TEXT NOT NULL, movie_overview TEXT NOT NULL, movie_release_date TEXT NOT NULL, movie_poster TEXT NOT NULL,
 movie_background TEXT NOT NULL, movie_popularity TEXT NOT NULL, movie_language TEXT NOT NULL, movie_vote_count INTEGER NOT NULL, movie_vote_avg REAL NOT NULL, UNIQUE ( movie_id ) ON CONFLICT REPLACE )
 */
public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final String TAG = MoviesDBHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "movies.db";

    // @formatter:off
    private static final String CREATE_MOVIES_TB =
            "CREATE TABLE " + MoviesDBContract.MoviesTB.TABLE_NAME +
                    " (" +
                    MoviesDBContract.MoviesTB._ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID       + " INTEGER NOT NULL, " +
                    MoviesDBContract.MoviesTB.COLUMN_TITLE          + " TEXT NOT NULL, " +
                    MoviesDBContract.MoviesTB.COLUMN_OVERVIEW       + " TEXT NOT NULL, " +
                    MoviesDBContract.MoviesTB.COLUMN_RELEASE_DATE   + " TEXT NOT NULL, " +
                    MoviesDBContract.MoviesTB.COLUMN_POSTER         + " TEXT NOT NULL, " +
                    MoviesDBContract.MoviesTB.COLUMN_BACKGROUND     + " TEXT NOT NULL, " +
                    MoviesDBContract.MoviesTB.COLUMN_POPULARITY     + " TEXT NOT NULL, " +
                    MoviesDBContract.MoviesTB.COLUMN_LANGUAGE       + " TEXT NOT NULL, " +
                    MoviesDBContract.MoviesTB.COLUMN_VOTE_COUNT     + " INTEGER NOT NULL, " +
                    MoviesDBContract.MoviesTB.COLUMN_VOTE_AVERAGE   + " REAL NOT NULL, " +
                    "UNIQUE ( " + MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID + " ) ON CONFLICT REPLACE )";

    private static final String CREATE_FAV_TB =
            "CREATE TABLE " + MoviesDBContract.FavouriteMoviesTB.TABLE_NAME +
                    " (" +
                    MoviesDBContract.FavouriteMoviesTB._ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MoviesDBContract.FavouriteMoviesTB.COLUMN_MOVIE_ID       + " INTEGER NOT NULL, " +
                    "UNIQUE ( " + MoviesDBContract.FavouriteMoviesTB.COLUMN_MOVIE_ID + " ) ON CONFLICT REPLACE " +
                    "FOREIGN KEY ( "+MoviesDBContract.FavouriteMoviesTB.COLUMN_MOVIE_ID + " ) REFERENCES " +
                    MoviesDBContract.MoviesTB.TABLE_NAME + " ( " + MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID + " ))";

    private static final String DROP_MOVIES_TB = "DROP TABLE IF EXISTS " + MoviesDBContract.MoviesTB.TABLE_NAME;
    private static final String DROP_FAV_TB = "DROP TABLE IF EXISTS " + MoviesDBContract.FavouriteMoviesTB.TABLE_NAME;
    // @formatter:on

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, CREATE_MOVIES_TB);
        Log.d(TAG, CREATE_FAV_TB);

        sqLiteDatabase.execSQL(CREATE_MOVIES_TB);
        sqLiteDatabase.execSQL(CREATE_FAV_TB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_FAV_TB);
        sqLiteDatabase.execSQL(DROP_MOVIES_TB);
        onCreate(sqLiteDatabase);
    }
}
