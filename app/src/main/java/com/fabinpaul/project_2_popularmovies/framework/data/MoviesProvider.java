package com.fabinpaul.project_2_popularmovies.framework.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;

import static com.fabinpaul.project_2_popularmovies.framework.data.MoviesDBContract.CONTENT_AUTHORITY;
import static com.fabinpaul.project_2_popularmovies.framework.data.MoviesDBContract.FavouriteMoviesTB;
import static com.fabinpaul.project_2_popularmovies.framework.data.MoviesDBContract.MoviesTB;
import static com.fabinpaul.project_2_popularmovies.framework.data.MoviesDBContract.PATH_FAV_MOVIES;
import static com.fabinpaul.project_2_popularmovies.framework.data.MoviesDBContract.PATH_MOVIES;

/**
 * Created by Fabin Paul on 11/25/2016 3:31 PM.
 */

public class MoviesProvider extends ContentProvider {

    private static final int FAV_MOVIE_ID_LIST = 100;
    private static final int FAV_MOVIE_LIST = 101;
    private static final int MOVIE = 102;
    private static final int MOVIE_LIST = 103;

    private MoviesDBHelper mMoviesDBHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sSQLiteQueryBuilder;

    static {
        sSQLiteQueryBuilder = new SQLiteQueryBuilder();
        sSQLiteQueryBuilder.setTables(FavouriteMoviesTB.TABLE_NAME + " INNER JOIN " + MoviesTB.TABLE_NAME +
                " ON " + FavouriteMoviesTB.TABLE_NAME + "." + FavouriteMoviesTB._ID +
                " = " + MoviesTB.TABLE_NAME + "." + MoviesTB._ID);
    }

    private static final String sMovieSelection = MoviesTB.TABLE_NAME + "." + MoviesTB.COLUMN_MOVIE_ID + " = ?";

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIES + "/#", MOVIE);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIES, MOVIE_LIST);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_FAV_MOVIES, FAV_MOVIE_ID_LIST);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_FAV_MOVIES + "/" + PATH_MOVIES, FAV_MOVIE_LIST);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMoviesDBHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String pSelection, String[] pSelectionArgs, String pSortOrder) {
        Cursor rCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                int movieId = MoviesTB.getMovieIdFromUri(uri);
                String[] selectionArgs = new String[]{String.valueOf(movieId)};
                rCursor = mMoviesDBHelper.getReadableDatabase().query(MoviesTB.TABLE_NAME,
                        projection,
                        sMovieSelection,
                        selectionArgs,
                        null, null,
                        pSortOrder);
                break;
            case MOVIE_LIST:
                rCursor = mMoviesDBHelper.getReadableDatabase().query(MoviesTB.TABLE_NAME,
                        projection,
                        pSelection,
                        pSelectionArgs,
                        null, null,
                        pSortOrder);
                break;
            case FAV_MOVIE_ID_LIST:
                rCursor = mMoviesDBHelper.getReadableDatabase().query(FavouriteMoviesTB.TABLE_NAME,
                        projection,
                        pSelection,
                        pSelectionArgs,
                        null, null,
                        pSortOrder);
                break;
            case FAV_MOVIE_LIST:
                rCursor = sSQLiteQueryBuilder.query(mMoviesDBHelper.getReadableDatabase(),
                        projection,
                        pSelection,
                        pSelectionArgs,
                        null, null,
                        pSortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null)
            rCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return rCursor;
    }

    @Nullable
    @Override
    public String getType(Uri pUri) {
        switch (sUriMatcher.match(pUri)) {
            case MOVIE_LIST:
                return MoviesTB.CONTENT_TYPE;
            case MOVIE:
                return MoviesTB.CONTENT_TYPE_ITEM;
            case FAV_MOVIE_LIST:
                return FavouriteMoviesTB.CONTENT_TYPE;
            case FAV_MOVIE_ID_LIST:
                return FavouriteMoviesTB.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + pUri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri pUri, ContentValues pContentValues) {
        Uri rUri;
        switch (sUriMatcher.match(pUri)) {
            case MOVIE_LIST: {
                long _id = mMoviesDBHelper.getWritableDatabase().insert(MoviesTB.TABLE_NAME, null, pContentValues);
                if (_id > 0) {
                    rUri = MoviesTB.buildMovieUri(pContentValues.getAsInteger(MoviesTB.COLUMN_MOVIE_ID));
                } else {
                    throw new SQLException("Failed to insert row into " + pUri);
                }
                break;
            }
            case FAV_MOVIE_ID_LIST: {
                long _id = mMoviesDBHelper.getWritableDatabase().insert(FavouriteMoviesTB.TABLE_NAME, null, pContentValues);
                if (_id > 0) {
                    rUri = FavouriteMoviesTB.buildFavMovieUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + pUri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + pUri);
        }
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(pUri, null);
        return rUri;
    }

    @Override
    public int delete(Uri pUri, String pSelection, String[] pSelectionArgs) {
        int rowsDeleted;
        if (null == pSelection)
            pSelection = "1";
        switch (sUriMatcher.match(pUri)) {
            case MOVIE_LIST:
                rowsDeleted = mMoviesDBHelper.getWritableDatabase().delete(MoviesTB.TABLE_NAME, pSelection, pSelectionArgs);
                break;
            case FAV_MOVIE_ID_LIST:
                rowsDeleted = mMoviesDBHelper.getWritableDatabase().delete(FavouriteMoviesTB.TABLE_NAME, pSelection, pSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + pUri);
        }
        if (rowsDeleted != 0 && getContext() != null)
            getContext().getContentResolver().notifyChange(pUri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri pUri, ContentValues pContentValues, String pSelection, String[] pSelectionArgs) {
        int rowsUpdated;
        switch (sUriMatcher.match(pUri)) {
            case MOVIE_LIST:
                rowsUpdated = mMoviesDBHelper.getWritableDatabase().update(MoviesTB.TABLE_NAME, pContentValues, pSelection, pSelectionArgs);
                break;
            case FAV_MOVIE_ID_LIST:
                rowsUpdated = mMoviesDBHelper.getWritableDatabase().update(FavouriteMoviesTB.TABLE_NAME, pContentValues, pSelection, pSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + pUri);
        }
        if (rowsUpdated != 0 && getContext() != null)
            getContext().getContentResolver().notifyChange(pUri, null);
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case MOVIE_LIST:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues contentValues : values) {
                        long _id = db.insert(MoviesTB.TABLE_NAME, null, contentValues);
                        if (_id != -1)
                            rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(uri, null);
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void shutdown() {
        mMoviesDBHelper.close();
        super.shutdown();
    }
}
