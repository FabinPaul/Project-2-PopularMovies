package com.fabinpaul.project_2_popularmovies.framework.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/23/2016 4:17 PM.
 */
@RunWith(AndroidJUnit4.class)
public class MoviesDBHelperTest {

    public static final String LOG_TAG = MoviesDBHelperTest.class.getSimpleName();

    MoviesDBHelper mMoviesDBHelper;

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(MoviesDBHelper.DATABASE_NAME);
        mMoviesDBHelper = new MoviesDBHelper(getTargetContext());
    }

    @Test
    public void createDb() throws Exception {

        SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'", null);
        assertEquals("Error: This means that the database has not been created correctly", true, c.moveToFirst());

        c.close();
    }

    @After
    public void tearDown() throws Exception {
        mMoviesDBHelper.close();
    }

}