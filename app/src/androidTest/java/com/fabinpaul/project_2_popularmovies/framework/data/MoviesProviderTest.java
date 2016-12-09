package com.fabinpaul.project_2_popularmovies.framework.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;

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
    private static final String FILE_TEST_PREFIX = "test_";

    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(getTargetContext(), FILE_TEST_PREFIX);
    }

    @Test
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext, MoviesProvider.class);

        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: MoviesProvider registered with authority " + providerInfo.authority + " instead of " + MoviesDBContract.CONTENT_AUTHORITY, MoviesDBContract.CONTENT_AUTHORITY, providerInfo.authority);
        } catch (PackageManager.NameNotFoundException e) {
            fail("Error: MoviesProvider is not register at " + mContext.getPackageName());
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
        DBTestUtils.validateCurrentRecord("Error: Check query function of MoviesContenProvider", moviesCursor, movieContentValues);
    }
}