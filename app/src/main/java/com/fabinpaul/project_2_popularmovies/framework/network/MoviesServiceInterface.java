package com.fabinpaul.project_1_popularmovies.framework.network;

import com.fabinpaul.project_1_popularmovies.features.movieshome.data.MovieList;

import rx.Subscription;

/**
 * Created by Fabin Paul on 11/6/2016 2:14 PM.
 */

public interface MoviesServiceInterface {

    interface MoviesServiceCallback<T> {
        void onSuccess(T movies);

        void onFailure(String message);
    }

    Subscription getPopularMovies(String apiKey, int page, MoviesServiceCallback<MovieList> pCallback);

    Subscription getTopRatedMovies(String apiKey, int page, MoviesServiceCallback<MovieList> pCallback);
}
