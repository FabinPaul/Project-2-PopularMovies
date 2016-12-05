package com.fabinpaul.project_2_popularmovies.framework.network;

import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.MovieList;

import rx.Subscription;

/**
 * Created by Fabin Paul on 11/6/2016 2:14 PM.
 */

public interface MoviesServiceInterface {

    interface MoviesServiceCallback<T> {
        void onSuccess(T movies);

        void onFailure(String message);
    }

    Subscription getMoviesList(@MoviesServiceApi.MovieSortTypes String moviesSort, String apiKey, int page, MoviesServiceCallback<MovieList> pCallback);

    Subscription getMovieDetails(int pMovieId, String apiKey, MoviesServiceCallback<MovieDetails> pCallback);
}
