package com.fabinpaul.project_2_popularmovies.framework.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import com.fabinpaul.project_2_popularmovies.framework.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/24/2016 10:13 AM.
 */
public class DBTestUtils {

    public static void validateCurrentRecord(String errorString, Cursor queryValues, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> value : valueSet) {
            String columnName = value.getKey();
            int columnIndex = queryValues.getColumnIndex(columnName);
            assertTrue("Column " + columnName + " was not found." + errorString, columnIndex != -1);
            String expectedValue = value.getValue().toString();
            String actualValue = queryValues.getString(columnIndex);
            assertEquals("Value " + actualValue + " did not match the expected value " + expectedValue + " " + errorString, expectedValue, actualValue);
        }
    }

    public static ContentValues create_Rush_MovieValues() {
        ContentValues contentValues = new ContentValues();

        // @formatter:off
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_TITLE,       "Rush");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_BACKGROUND,  "/bXRrKLsOgx6Fb1LlmlAezblU7uQ.jpg");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_LANGUAGE,    "en");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID,    96721);
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_OVERVIEW,    "A biographical drama centered on the rivalry between Formula 1 drivers James Hunt and Niki Lauda during the 1976 Formula One motor-racing season.");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_POPULARITY,  "3.234442");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_POSTER,      "/cjEepHZOZAwmK6nAj5jis6HV75E.jpg");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_RELEASE_DATE,"2013-08-25");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_VOTE_AVERAGE,7.7);
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_VOTE_COUNT,  1414);
        // @formatter:on
        return contentValues;
    }

    public static ContentValues updated_Rush_MovieValues() {
        ContentValues contentValues = new ContentValues();

        // @formatter:off
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_TITLE,       "Rush");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_BACKGROUND,  "/bXRrKLsOgx6Fb1LlmlAezblU7uQ.jpg");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_LANGUAGE,    "en");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID,    96721);
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_OVERVIEW,    "A biographical drama centered on the rivalry between Formula 1 drivers James Hunt and Niki Lauda during the 1976 Formula One motor-racing season.");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_POPULARITY,  "3.234442");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_POSTER,      "/cjEepHZOZAwmK6nAj5jis6HV75E.jpg");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_RELEASE_DATE,"2013-09-02");
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_VOTE_AVERAGE,7.7);
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_VOTE_COUNT,  1414);
        // @formatter:on
        return contentValues;
    }

    public static ContentValues create_Fav_MovieValue(int movieId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesDBContract.FavouriteMoviesTB.COLUMN_MOVIE_ID, movieId);
        return contentValues;
    }

    static class TestContentObserver extends ContentObserver {

        final HandlerThread mHandlerThread;
        boolean mContentChanged;

        static TestContentObserver getInstance() {
            HandlerThread handlerThread = new HandlerThread("ContentObserverThread");
            handlerThread.start();
            return new TestContentObserver(handlerThread);
        }

        private TestContentObserver(HandlerThread handlerThread) {
            super(new Handler(handlerThread.getLooper()));
            mHandlerThread = handlerThread;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            new PollingCheck(5000) {

                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHandlerThread.quit();
        }
    }
}
