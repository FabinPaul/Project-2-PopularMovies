package com.fabinpaul.project_2_popularmovies.framework.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 12/9/2016 11:53 AM.
 */
@RunWith(AndroidJUnit4.class)
public class MoviesUriMatcherTest {

    private static final int MOVIE_ID = 157336;

    private static final Uri MOVIE_URI = MoviesDBContract.MoviesTB.buildMovieUri(MOVIE_ID);
    private static final Uri MOVIE_LIST_URI = MoviesDBContract.MoviesTB.CONTENT_URI;
    private static final Uri FAV_ID_LIST_URI = MoviesDBContract.FavouriteMoviesTB.CONTENT_URI;
    private static final Uri FAV_MOVIE_LIST_URI = MoviesDBContract.FavouriteMoviesTB.buildFavMoveListUri();

    @Test
    public void testUriMatcher() {
        UriMatcher uriMatcher = MoviesProvider.buildUriMatcher();

        assertEquals("Error: MOVIE_URI matched incorrectly.", MoviesProvider.MOVIE, uriMatcher.match(MOVIE_URI));
        assertEquals("Error: MOVIE_LIST_URI matched incorrectly.", MoviesProvider.MOVIE_LIST, uriMatcher.match(MOVIE_LIST_URI));
        assertEquals("Error: FAV_MOVIE_ID_LIST matched incorrectly.", MoviesProvider.FAV_MOVIE_ID_LIST, uriMatcher.match(FAV_ID_LIST_URI));
        assertEquals("Error: FAV_MOVIE_LIST matched incorrectly.", MoviesProvider.FAV_MOVIE_LIST, uriMatcher.match(FAV_MOVIE_LIST_URI));
    }
}
