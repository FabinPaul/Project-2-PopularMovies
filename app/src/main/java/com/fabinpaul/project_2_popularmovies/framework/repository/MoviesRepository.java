package com.fabinpaul.project_2_popularmovies.framework.repository;

import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.MovieList;

/**
 * Created by Fabin Paul on 11/6/2016 3:35 PM.
 */

public interface MoviesRepository {

    interface MoviesRepositoryCallback<T> {
        void onSuccess(T movies);

        void onFailure(String message);
    }

    void onDestroy();

    void onStop();

    void onStart();

    Movie getMovie(int atPosition);

    int getTopPages();

    int getTotalMoviesCount();

    void clearMoviesList();

    void getPopularMovies(int pageNoToLoad, MoviesRepository.MoviesRepositoryCallback<MovieList> pCallback, boolean isRefresh);

    void getTopRatedMovies(int pageNoToLoad, MoviesRepository.MoviesRepositoryCallback<MovieList> pCallback, boolean isRefresh);

    void getMovieDetails(int movieId, MoviesRepository.MoviesRepositoryCallback<MovieDetails> pCallback);
}
