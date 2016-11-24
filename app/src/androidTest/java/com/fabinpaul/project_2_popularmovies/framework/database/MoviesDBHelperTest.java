package com.fabinpaul.project_2_popularmovies.framework.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/23/2016 4:17 PM.
 */
@RunWith(AndroidJUnit4.class)
public class MoviesDBHelperTest {

    public static final String LOG_TAG = MoviesDBHelperTest.class.getSimpleName();
    private static final String FILE_TEST_PREFIX = "test_";
    private MoviesDBHelper mMoviesDBHelper;
    private RenamingDelegatingContext mDelegatingContext;

    @Before
    public void setUp() throws Exception {
        mDelegatingContext = new RenamingDelegatingContext(getTargetContext(), FILE_TEST_PREFIX);
        //mDelegatingContext.deleteDatabase(FILE_TEST_PREFIX + MoviesDBHelper.DATABASE_NAME);
        mMoviesDBHelper = new MoviesDBHelper(mDelegatingContext);
    }

    @Test
    public void createDb() throws Exception {

        HashSet<String> tablesNamesHashSet = new HashSet<>();
        tablesNamesHashSet.add(MoviesDBContract.MoviesTB.TABLE_NAME);
        tablesNamesHashSet.add(MoviesDBContract.FavouriteMoviesTB.TABLE_NAME);

        SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'", null);
        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        do {
            tablesNamesHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error: Database was created without Movies or Favourite tables", tablesNamesHashSet.isEmpty());

        HashSet<String> moviesColumnHashSet = new HashSet<>();
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB._ID);
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB.COLUMN_TITLE);
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB.COLUMN_BACKGROUND);
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB.COLUMN_LANGUAGE);
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID);
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB.COLUMN_OVERVIEW);
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB.COLUMN_POPULARITY);
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB.COLUMN_POSTER);
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB.COLUMN_RELEASE_DATE);
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB.COLUMN_VOTE_AVERAGE);
        moviesColumnHashSet.add(MoviesDBContract.MoviesTB.COLUMN_VOTE_COUNT);

        c = db.rawQuery("PRAGMA table_info (" + MoviesDBContract.MoviesTB.TABLE_NAME + ")", null);

        assertTrue("Error: Unable to query database for movies table info", c.moveToFirst());

        int columnNameIndex = c.getColumnIndex("name");

        do {
            moviesColumnHashSet.remove(c.getString(columnNameIndex));
        } while (c.moveToNext());

        assertTrue("Error: Database doesn't contain all of the required movie table columns", moviesColumnHashSet.isEmpty());

        HashSet<String> favColumnHashSet = new HashSet<>();
        moviesColumnHashSet.add(MoviesDBContract.FavouriteMoviesTB._ID);
        moviesColumnHashSet.add(MoviesDBContract.FavouriteMoviesTB.COLUMN_MOVIE_ID);

        c = db.rawQuery("PRAGMA table_info (" + MoviesDBContract.FavouriteMoviesTB.TABLE_NAME + ")", null);

        assertTrue("Error: Unable to query database for movies table info", c.moveToFirst());

        columnNameIndex = c.getColumnIndex("name");

        do {
            favColumnHashSet.remove(c.getString(columnNameIndex));
        } while (c.moveToNext());

        assertTrue("Error: Database doesn't contain all of the required movie table columns", favColumnHashSet.isEmpty());
        c.close();
        db.close();
    }

    @Test
    public void testInvalidFavRecord() {
        int movieId = 1234;

        SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();

        ContentValues testValues = DBTestUtils.create_Fav_MovieValue(movieId);

        try {
            long favRowId = db.insert(MoviesDBContract.FavouriteMoviesTB.TABLE_NAME, null, testValues);
            fail("Error: record should not be inserted");
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFavouriteTB() {

        int movieId = insertMovie();

        SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();

        ContentValues testValues = DBTestUtils.create_Fav_MovieValue(movieId);

        long favRowId = db.insert(MoviesDBContract.FavouriteMoviesTB.TABLE_NAME, null, testValues);

        assertTrue(favRowId != -1);

        Cursor c = db.query(MoviesDBContract.FavouriteMoviesTB.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No records found in favourite movie table", c.moveToFirst());

        DBTestUtils.validateCurrentRecord("Error: Favourite record/item validation failed", c, testValues);

        assertFalse("Error: More than one record found in favourite movie table", c.moveToNext());

        c.close();
        db.close();
    }

    @Test
    public void testMovieTB() {
        insertMovie();
    }

    public int insertMovie() {
        SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();

        ContentValues testValues = DBTestUtils.create_Rush_MovieValues();

        long movieRowId = db.insert(MoviesDBContract.MoviesTB.TABLE_NAME, null, testValues);

        assertTrue(movieRowId != -1);

        Cursor c = db.query(MoviesDBContract.MoviesTB.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No records found in movies table", c.moveToFirst());

        DBTestUtils.validateCurrentRecord("Error: Movie record/item validation failed", c, testValues);

        assertFalse("Error: More than one record found in movies table", c.moveToNext());

        c.close();
        db.close();
        return testValues.getAsInteger(MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID);
    }

    @After
    public void tearDown() throws Exception {
        mMoviesDBHelper.close();
    }

}