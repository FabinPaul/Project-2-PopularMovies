package com.fabinpaul.project_2_popularmovies.framework.repository;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.Video;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;

import java.util.ArrayList;

/**
 * Created by Fabin Paul on 11/6/2016 3:35 PM.
 */

public interface MoviesRepository {

    String MOVIE_EXTRA = "com.fabinpaul.project_2_popularmovies.MovieExtra";
    String MOVIE_LIST_EXTRA = "com.fabinpaul.project_2_popularmovies.MovieListExtra";
    String MOVIE_DETAILS_EXTRA = "com.fabinpaul.project_2_popularmovies.MovieDetailsExtra";
    //String REVIEW_LIST_EXTRA = "com.fabinpaul.project_2_popularmovies.ReviewList";

    interface MoviesRepositoryCallback {
        void onSuccess();

        void onFailure(String message);
    }

    interface MoviesLoaderCallback {
        void onSuccess();

        void onReset();
    }

    void onStop();

    void onStart();

    void onSaveStateInstance(Bundle savedInstanceState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onDestroy();

    Movie getMovie(int atPosition);

    int getTopPages();

    int getTotalMoviesCount();

    void clearMoviesList();

    ArrayList<Video> getVideoList();

    void getPopularMovies(int pageNoToLoad, @NonNull MoviesRepository.MoviesRepositoryCallback pCallback, boolean isRefresh);

    void getTopRatedMovies(int pageNoToLoad, @NonNull MoviesRepository.MoviesRepositoryCallback pCallback, boolean isRefresh);

    void getMovieDetails(int movieId, @NonNull MoviesRepository.MoviesRepositoryCallback pCallback);

    MovieDetails getMovieDetails();

    void setMovieAsFavourite(boolean isFav);

    void getFavouriteMovies(@NonNull MoviesRepository.MoviesLoaderCallback pCallback);

    void loadFavouriteMovieId();

    boolean isFavouriteMovie(int pMovieId);
}
