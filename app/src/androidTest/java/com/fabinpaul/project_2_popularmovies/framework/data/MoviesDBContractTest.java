package com.fabinpaul.project_2_popularmovies.framework.data;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 12/9/2016 10:18 AM.
 */
@RunWith(AndroidJUnit4.class)
public class MoviesDBContractTest {

    private static final int MOVIE_ID = 157336;

    @Test
    public void testMovieUriBuilder() {
        Uri movieUri = MoviesDBContract.MoviesTB.buildMovieUri(MOVIE_ID);

        assertNotNull("Error: Null movie uri returned. Add implementation for buildFavMovieUri in MoviesDBContract.MoviesTB", movieUri);

        assertEquals("Error: Movie id not properly appended to uri", MOVIE_ID, MoviesDBContract.MoviesTB.getMovieIdFromUri(movieUri));

        assertEquals("Error: Movie uri doesn't match the expected result", "content://com.fabinpaul.project_2_popularmovies/movies/157336", movieUri.toString());
    }
}