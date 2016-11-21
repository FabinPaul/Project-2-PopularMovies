package com.fabinpaul.project_1_popularmovies.features.movieshome.logic;

import android.support.annotation.IntDef;

import com.fabinpaul.project_1_popularmovies.features.movieshome.data.Movie;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Fabin Paul on 11/5/2016 1:35 PM.
 */

public interface MoviesListContract {

    int POPULAR_MOVIE = 1;
    int TOP_RATED_MOVIE = 2;

    interface View{
        void getPopularMovies();

        void getTopRatedMovies();

        void showMovieDetails(Movie pMovie);
    }

    interface UserActionsListener{
        void moviesOnClick(int pPosition);

        void changeMovieSort(@MovieSortStatus int pSortId);

        void loadMoreDataFromApi(int pPageToLoad);
    }


    @Retention(RetentionPolicy.SOURCE)
    // Enumerate valid values for this interface
    @IntDef({ POPULAR_MOVIE,TOP_RATED_MOVIE })
    // Create an interface for validating int types
    @interface MovieSortStatus { }
}
