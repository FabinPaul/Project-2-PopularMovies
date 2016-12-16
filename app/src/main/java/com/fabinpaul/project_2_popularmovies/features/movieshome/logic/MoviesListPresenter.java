package com.fabinpaul.project_2_popularmovies.features.movieshome.logic;

import android.support.annotation.NonNull;

import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_2_popularmovies.framework.repository.MoviesRepository;

/**
 * Created by Fabin Paul on 11/5/2016 1:37 PM.
 */

public class MoviesListPresenter implements MoviesListContract.UserActionsListener {

    private final MoviesListContract.View mView;
    private final MoviesRepository mMoviesRepository;
    private int mCurrentMovieSortId = 0;
    private MoviesRepository.MoviesRepositoryCallback mCurrentCallback;

    public MoviesListPresenter(MoviesListContract.View pView, MoviesRepository pMoviesRepository) {
        mView = pView;
        mMoviesRepository = pMoviesRepository;
    }

    public int getTotalMoviesCount() {
        return mMoviesRepository.getTotalMoviesCount();
    }

    public Movie getMovie(int atPosition) {
        return mMoviesRepository.getMovie(atPosition);
    }

    public void getPopularMovies(int pPageToLoad, @NonNull MoviesRepository.MoviesRepositoryCallback pCallback) {
        mCurrentCallback = pCallback;
        mMoviesRepository.getPopularMovies(pPageToLoad, pCallback, false);
    }

    public void getTopRatedMovies(int pPageToLoad, @NonNull MoviesRepository.MoviesRepositoryCallback pCallback) {
        mCurrentCallback = pCallback;
        mMoviesRepository.getTopRatedMovies(pPageToLoad, pCallback, false);
    }

    public void getFavouriteMovies(@NonNull MoviesRepository.MoviesLoaderCallback pCallback) {
        mCurrentCallback = null;
        mMoviesRepository.getFavouriteMovies(pCallback);
    }

    public void loadFavouriteMovies() {
        mMoviesRepository.loadFavouriteMovieId();
    }

    public boolean isFavouriteMovie(int pMovieId) {
        return mMoviesRepository.isFavouriteMovie(pMovieId);
    }

    @Override
    public void moviesOnClick(int pPosition) {
        mView.showMovieDetails(mMoviesRepository.getMovie(pPosition));
    }

    @Override
    public void changeMovieSort(int pSortId) {
        mCurrentMovieSortId = pSortId;
        mMoviesRepository.clearMoviesList();
        if (mCurrentMovieSortId == MoviesListContract.POPULAR_MOVIE) {
            mView.getPopularMovies();
        } else if (pSortId == MoviesListContract.FAVOURITE_MOVIE) {
            mView.getFavouriteMovies();
        } else {
            mView.getTopRatedMovies();
        }
    }

    @Override
    public void loadMoreDataFromApi(int pPageToLoad) {
        if (mMoviesRepository.getTopPages() > 0 && pPageToLoad > mMoviesRepository.getTopPages())
            return;
        switch (mCurrentMovieSortId) {
            case MoviesListContract.POPULAR_MOVIE:
                mMoviesRepository.getPopularMovies(pPageToLoad, mCurrentCallback, false);
                break;
            case MoviesListContract.TOP_RATED_MOVIE:
                mMoviesRepository.getTopRatedMovies(pPageToLoad, mCurrentCallback, false);
                break;
        }
    }

    @Override
    public void refreshPage() {
        mMoviesRepository.clearMoviesList();
        if (mCurrentMovieSortId == MoviesListContract.POPULAR_MOVIE) {
            mMoviesRepository.getPopularMovies(1, mCurrentCallback, true);
        } else if (mCurrentMovieSortId == MoviesListContract.FAVOURITE_MOVIE) {
            mView.getFavouriteMovies();
        } else {
            mMoviesRepository.getTopRatedMovies(1, mCurrentCallback, true);
        }
    }
}
