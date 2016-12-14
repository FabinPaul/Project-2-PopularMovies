package com.fabinpaul.project_2_popularmovies.framework.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 12/9/2016 10:53 AM.
 */
@RunWith(AndroidJUnit4.class)
public class MoviesProviderTest {

    public static final String TAG = MoviesProviderTest.class.getSimpleName();
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = getTargetContext();
    }

    @Test
    public void testProviderRegistry() {
        PackageManager pm = getTargetContext().getPackageManager();

        ComponentName componentName = new ComponentName(getTargetContext(), MoviesProvider.class);

        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: MoviesProvider registered with authority " + providerInfo.authority + " instead of " + MoviesDBContract.CONTENT_AUTHORITY, MoviesDBContract.CONTENT_AUTHORITY, providerInfo.authority);
        } catch (PackageManager.NameNotFoundException e) {
            fail("Error: MoviesProvider is not register at " + getTargetContext().getPackageName());
        }
    }

    @Test
    public void testGetType() {
        String type = mContext.getContentResolver().getType(MoviesDBContract.MoviesTB.CONTENT_URI);
        assertEquals("Error: Movies list should return MoviesTB.CONTENT_TYPE ", MoviesDBContract.MoviesTB.CONTENT_TYPE, type);

        int moviesId = 157336;
        type = mContext.getContentResolver().getType(MoviesDBContract.MoviesTB.buildMovieUri(moviesId));
        assertEquals("Error: Movie item should return MoviesTB.CONTENT_TYPE_ITEM ", MoviesDBContract.MoviesTB.CONTENT_TYPE_ITEM, type);

        type = mContext.getContentResolver().getType(MoviesDBContract.FavouriteMoviesTB.CONTENT_URI);
        assertEquals("Error: Favourite movie id list should return FavouriteMoviesTB.CONTENT_TYPE", MoviesDBContract.FavouriteMoviesTB.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(MoviesDBContract.FavouriteMoviesTB.buildFavMoveListUri());
        assertEquals("Error: Favourite Movies list should return FavouriteMoviesTB.CONTENT_TYPE", MoviesDBContract.FavouriteMoviesTB.CONTENT_TYPE, type);
    }

    @Test
    public void testBasicQuery() {
        MoviesDBHelper dbHelper = new MoviesDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues movieContentValues = DBTestUtils.create_Rush_MovieValues();
        db.insert(MoviesDBContract.MoviesTB.TABLE_NAME, null, movieContentValues);

        ContentValues favContentValues = DBTestUtils.create_Fav_MovieValue(movieContentValues.getAsInteger(MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID));
        db.insert(MoviesDBContract.FavouriteMoviesTB.TABLE_NAME, null, favContentValues);

        db.close();

        Cursor moviesCursor = mContext.getContentResolver().query(MoviesDBContract.FavouriteMoviesTB.buildFavMoveListUri(),
                null,
                null,
                null,
                null);

        assertTrue("Error: Movie list was not fetched from content provider", moviesCursor != null && moviesCursor.moveToFirst());
        DBTestUtils.validateCurrentRecord("Error: Check query function of MoviesContentProvider", moviesCursor, movieContentValues);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            assertEquals("Error: Query didn't properly set Notification Uri", moviesCursor.getNotificationUri(), MoviesDBContract.FavouriteMoviesTB.buildFavMoveListUri());
        }
    }

    @Test
    public void testUpdateProvider() {
        ContentValues movieNew = DBTestUtils.create_Rush_MovieValues();

        Uri movieUri = mContext.getContentResolver().insert(MoviesDBContract.MoviesTB.CONTENT_URI, movieNew);
        int movieId = MoviesDBContract.MoviesTB.getMovieIdFromUri(movieUri);

        assertTrue("Error: Movie not inserted. Check " + movieUri + " insert in MoviesProvider", movieId != -1);

        ContentValues movieUpdate = DBTestUtils.updated_Rush_MovieValues();

        Cursor movieCursor = mContext.getContentResolver().query(MoviesDBContract.MoviesTB.buildMovieUri(movieId), null, null, null, null);

        assertTrue("Error: Movie not queried.. Check " + MoviesDBContract.MoviesTB.buildMovieUri(movieId) +
                " query in MoviesProvider", movieCursor != null && movieCursor.moveToFirst());

        DBTestUtils.TestContentObserver observer = DBTestUtils.TestContentObserver.getInstance();
        movieCursor.registerContentObserver(observer);

        int count = mContext.getContentResolver().update(MoviesDBContract.MoviesTB.CONTENT_URI, movieUpdate, MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(movieId)});

        assertTrue("Error: Update failed. Check" + MoviesDBContract.MoviesTB.CONTENT_URI + " update in MoviesProvider", count == 1);

        observer.waitForNotificationOrFail();

        movieCursor.unregisterContentObserver(observer);
        movieCursor.close();

        Cursor movie = mContext.getContentResolver().query(MoviesDBContract.MoviesTB.CONTENT_URI, null, MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(movieId)}, null);

        assertTrue("Error: Update failed. Check " + MoviesDBContract.MoviesTB.CONTENT_URI + " update in MoviesProvider", movie != null && movie.moveToFirst());

        DBTestUtils.validateCurrentRecord("Error: Check query function of MoviesContentProvider ", movie, movieUpdate);

        movie.close();
    }

    @Test
    public void testInsertProvider() {
        ContentValues moviesNew = DBTestUtils.create_Rush_MovieValues();

        DBTestUtils.TestContentObserver movieObserver = DBTestUtils.TestContentObserver.getInstance();
        mContext.getContentResolver().registerContentObserver(MoviesDBContract.MoviesTB.CONTENT_URI, true, movieObserver);

        Uri moviesUri = mContext.getContentResolver().insert(MoviesDBContract.MoviesTB.CONTENT_URI, moviesNew);

        movieObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(movieObserver);

        int movieId = MoviesDBContract.MoviesTB.getMovieIdFromUri(moviesUri);

        assertTrue("Error: Movie not inserted.  Check " + moviesUri + " insert in MoviesProvider", movieId != -1);

        Cursor movieCursor = mContext.getContentResolver().query(MoviesDBContract.MoviesTB.buildMovieUri(movieId), null, null, null, null);

        assertTrue("Error: Movie not queried.. Check " + MoviesDBContract.MoviesTB.buildMovieUri(movieId) +
                " query in MoviesProvider", movieCursor != null && movieCursor.moveToFirst());


        DBTestUtils.validateCurrentRecord("Error: Movie validation failed", movieCursor, moviesNew);

        movieCursor.close();

        ContentValues favouriteNew = DBTestUtils.create_Fav_MovieValue(moviesNew.getAsInteger(MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID));

        DBTestUtils.TestContentObserver favObserver = DBTestUtils.TestContentObserver.getInstance();
        mContext.getContentResolver().registerContentObserver(MoviesDBContract.FavouriteMoviesTB.CONTENT_URI, true, favObserver);

        Uri favUri = mContext.getContentResolver().insert(MoviesDBContract.FavouriteMoviesTB.CONTENT_URI, favouriteNew);


        favObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(favObserver);

        int favId = MoviesDBContract.FavouriteMoviesTB.getFavIdFromUri(moviesUri);

        assertTrue("Error: Fav Movie id not inserted.  Check " + favUri + " insert in MoviesProvider", favId != -1);

        Cursor favCursor = mContext.getContentResolver().query(MoviesDBContract.FavouriteMoviesTB.CONTENT_URI, null,
                MoviesDBContract.FavouriteMoviesTB.COLUMN_MOVIE_ID + " =?", new String[]{String.valueOf(favId)}, null);

        assertTrue("Error: Fav Movie not queried.. Check " + MoviesDBContract.FavouriteMoviesTB.CONTENT_URI +
                " query in MoviesProvider", favCursor != null && favCursor.moveToFirst());

        DBTestUtils.validateCurrentRecord("Error: Fav Movie id validation failed", favCursor, favouriteNew);

        moviesNew.putAll(favouriteNew);

        Cursor favMovieCursor = mContext.getContentResolver().query(MoviesDBContract.FavouriteMoviesTB.buildFavMoveListUri(), null,
                MoviesDBContract.MoviesTB.TABLE_NAME + "." + MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(movieId)}, null);

        assertTrue("Error: Fav Movie not queried.. Check " + MoviesDBContract.FavouriteMoviesTB.buildFavMoveListUri() +
                " query in MoviesProvider", favMovieCursor != null && favMovieCursor.moveToFirst());

        DBTestUtils.validateCurrentRecord("Error: Fav Movie validation failed", favMovieCursor, moviesNew);

        favMovieCursor.close();
    }

    @Test
    public void testDeleteProvider() {
        testInsertProvider();

        DBTestUtils.TestContentObserver movieObserver = DBTestUtils.TestContentObserver.getInstance();
        DBTestUtils.TestContentObserver favMovieObserver = DBTestUtils.TestContentObserver.getInstance();

        mContext.getContentResolver().registerContentObserver(MoviesDBContract.MoviesTB.CONTENT_URI, true, movieObserver);
        mContext.getContentResolver().registerContentObserver(MoviesDBContract.FavouriteMoviesTB.CONTENT_URI, true, favMovieObserver);

        int favCount = mContext.getContentResolver().delete(MoviesDBContract.FavouriteMoviesTB.CONTENT_URI, null, null);
        int movieCount = mContext.getContentResolver().delete(MoviesDBContract.MoviesTB.CONTENT_URI, null, null);

        assertTrue("Error deleting fav movies", favCount >= 1);
        assertTrue("Error deleting movies", movieCount >= 1);

        Cursor favCursor = mContext.getContentResolver().query(MoviesDBContract.FavouriteMoviesTB.CONTENT_URI, null, null, null, null);
        Cursor movieCursor = mContext.getContentResolver().query(MoviesDBContract.FavouriteMoviesTB.CONTENT_URI, null, null, null, null);


        assert movieCursor != null;
        assertEquals("Error not all movies where deleted", 0, movieCursor.getCount());
        assert favCursor != null;
        assertEquals("Error not all fav id where deleted", 0, favCursor.getCount());
        movieCursor.close();
        favCursor.close();

        movieObserver.waitForNotificationOrFail();
        favMovieObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(movieObserver);
        mContext.getContentResolver().unregisterContentObserver(favMovieObserver);
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    @Test
    public void testBulkInsertProvider() {
        ContentValues[] bulkMovieValues = DBTestUtils.create_Bulk_MovieValues(BULK_INSERT_RECORDS_TO_INSERT);

        DBTestUtils.TestContentObserver movieObserver = DBTestUtils.TestContentObserver.getInstance();
        mContext.getContentResolver().registerContentObserver(MoviesDBContract.MoviesTB.CONTENT_URI, true, movieObserver);

        int movieCount = mContext.getContentResolver().bulkInsert(MoviesDBContract.MoviesTB.CONTENT_URI, bulkMovieValues);

        movieObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(movieObserver);

        assertEquals("Error: Not all movie inserted in bulk insert", movieCount, BULK_INSERT_RECORDS_TO_INSERT);

        Cursor movieCursor = mContext.getContentResolver().query(MoviesDBContract.MoviesTB.CONTENT_URI, null, null, null, MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID + " ASC");

        assert movieCursor != null;
        assertEquals("Error validation movie bulk insert count", movieCursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        movieCursor.moveToFirst();

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, movieCursor.moveToNext()) {
            DBTestUtils.validateCurrentRecord("Error: validation movie record at postion " + i, movieCursor, bulkMovieValues[i]);
        }

        movieCursor.close();
    }
}