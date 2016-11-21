package com.fabinpaul.project_1_popularmovies.features.movieshome.logic;

import android.support.annotation.NonNull;

import com.fabinpaul.project_1_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_1_popularmovies.features.movieshome.data.MovieList;
import com.fabinpaul.project_1_popularmovies.framework.repository.MoviesRepository;

/**
 * Created by Fabin Paul on 11/5/2016 1:37 PM.
 */

public class MoviesListPresenter implements MoviesListContract.UserActionsListener {

    private final MoviesListContract.View mView;
    private final MoviesRepository mMoviesRepository;
    private int mCurrentMovieSortId = 0;
    private MoviesRepository.MoviesRepositoryCallback<MovieList> mCurrentCallback;

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

    public void getPopularMovies(int pPageToLoad,@NonNull MoviesRepository.MoviesRepositoryCallback<MovieList> pCallback) {
        mCurrentCallback = pCallback;
        mMoviesRepository.getPopularMovies(pPageToLoad, pCallback);
    }

    public void getTopRatedMovies(int pPageToLoad,@NonNull MoviesRepository.MoviesRepositoryCallback<MovieList> pCallback) {
        mCurrentCallback = pCallback;
        mMoviesRepository.getTopRatedMovies(pPageToLoad, pCallback);
    }

    @Override
    public void moviesOnClick(int pPosition) {
        mView.showMovieDetails(mMoviesRepository.getMovie(pPosition));
    }

    @Override
    public void changeMovieSort(int pSortId) {
        mCurrentMovieSortId = pSortId;
        mMoviesRepository.clearMoviesList();
        if(pSortId==MoviesListContract.POPULAR_MOVIE){
            mView.getPopularMovies();
        }else{
            mView.getTopRatedMovies();
        }
    }

    @Override
    public void loadMoreDataFromApi(int pPageToLoad) {
        if (mMoviesRepository.getTopPages() > 0 && pPageToLoad > mMoviesRepository.getTopPages())
            return;
        switch (mCurrentMovieSortId) {
            case MoviesListContract.POPULAR_MOVIE:
                mMoviesRepository.getPopularMovies(pPageToLoad, mCurrentCallback);
                break;
            case MoviesListContract.TOP_RATED_MOVIE:
                mMoviesRepository.getTopRatedMovies(pPageToLoad, mCurrentCallback);
                break;
        }
    }
}
