package com.fabinpaul.project_2_popularmovies.features.movieshome.logic;

import android.support.annotation.IntDef;

import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Fabin Paul on 11/5/2016 1:35 PM.
 */

public interface MoviesListContract {

    int POPULAR_MOVIE = 1;
    int TOP_RATED_MOVIE = 2;
    int FAVOURITE_MOVIE = 3;

    interface View {
        void getPopularMovies();

        void getTopRatedMovies();

        void showMovieDetails(Movie pMovie, int pPosition);

        void getFavouriteMovies();
    }

    interface UserActionsListener {
        void moviesOnClick(int pPosition);

        void changeMovieSort(@MovieSortStatus int pSortId);

        void loadMoreDataFromApi(int pPageToLoad);

        void refreshPage();
    }


    @Retention(RetentionPolicy.SOURCE)
    // Enumerate valid values for this interface
    @IntDef({POPULAR_MOVIE, TOP_RATED_MOVIE, FAVOURITE_MOVIE})
            // Create an interface for validating int types
    @interface MovieSortStatus {
    }
}
