package com.fabinpaul.project_2_popularmovies.features.moviesdetail.logic;

import android.support.annotation.NonNull;

import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
import com.fabinpaul.project_2_popularmovies.framework.repository.MoviesRepository;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 12/5/2016 12:36 PM.
 */

public class MovieDetailsPresenter implements MovieDetailsContract.UserInteractions {

    private MovieDetailsContract.View mView;
    private MoviesRepository mMoviesRepository;

    public MovieDetailsPresenter(MovieDetailsContract.View view, MoviesRepository moviesRepository) {
        mView = view;
        mMoviesRepository = moviesRepository;
    }

    public void getMoviesDetails(int pMovieId, @NonNull MoviesRepository.MoviesRepositoryCallback<MovieDetails> pCallback) {
        mMoviesRepository.getMovieDetails(pMovieId, pCallback);
    }
}
