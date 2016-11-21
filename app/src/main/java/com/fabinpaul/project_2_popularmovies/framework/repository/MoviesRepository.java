package com.fabinpaul.project_1_popularmovies.framework.repository;

import com.fabinpaul.project_1_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_1_popularmovies.features.movieshome.data.MovieList;

/**
 * Created by Fabin Paul on 11/6/2016 3:35 PM.
 */

public interface MoviesRepository {

    interface MoviesRepositoryCallback<T> {
        void onSuccess(T movies);

        void onFailure(String message);
    }

    void onDestroy();

    Movie getMovie(int atPosition);

    int getTopPages();

    int getTotalMoviesCount();

    void clearMoviesList();

    void getPopularMovies(int pageNoToLoad, MoviesRepository.MoviesRepositoryCallback<MovieList> pCallback);

    void getTopRatedMovies(int pageNoToLoad, MoviesRepository.MoviesRepositoryCallback<MovieList> pCallback);
}
