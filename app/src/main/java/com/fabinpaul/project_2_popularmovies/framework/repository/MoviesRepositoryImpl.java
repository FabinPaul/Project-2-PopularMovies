package com.fabinpaul.project_2_popularmovies.framework.repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.fabinpaul.project_2_popularmovies.R;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.Video;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.MovieList;
import com.fabinpaul.project_2_popularmovies.framework.network.CacheImpl;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceApi;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceInterface;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Fabin Paul on 11/6/2016 5:06 PM.
 */

public class MoviesRepositoryImpl implements MoviesRepository {

    private static final int PAGE_FROM_WHICH_LOADING_NOT_SHOWN = 0;
    private static final int PAGE_FROM_WHICH_DISMISS_NOT_CALLED = 1;
    private final MoviesServiceInterface mMoviesServiceInterface;
    private CompositeSubscription mCompositeSubscription;
    private MovieList mMovieList;
    private MovieDetails mMovieDetails;
    private String mApiKey;
    private boolean isLoading;

    private ProgressDialog mProgressDialog;
    private Context mContext;

    public MoviesRepositoryImpl(Context pContext, MoviesServiceInterface pMoviesServiceInterface) {
        mContext = pContext;
        mMoviesServiceInterface = pMoviesServiceInterface;
        mMovieList = new MovieList();
        mMovieDetails = new MovieDetails();

        mCompositeSubscription = new CompositeSubscription();
        mProgressDialog = new ProgressDialog(pContext);
        mProgressDialog.setIndeterminate(true);

        mApiKey = mContext.getString(R.string.api_key);
    }

    @Override
    public void onDestroy() {
        if (mCompositeSubscription != null)
            mCompositeSubscription.unsubscribe();
        if (mContext != null)
            mContext = null;
        if (mProgressDialog != null)
            mProgressDialog = null;
    }

    @Override
    public void onStop() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        if (isLoading) {
            mProgressDialog.show();
        }
    }

    @Override
    public void onSaveStateInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable(MOVIE_LIST_EXTRA, mMovieList);
            savedInstanceState.putParcelable(MOVIE_DETAILS_EXTRA, mMovieDetails);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelable(MOVIE_LIST_EXTRA);
            mMovieDetails = savedInstanceState.getParcelable(MOVIE_DETAILS_EXTRA);
        }
    }

    @Override
    public Movie getMovie(int atPosition) {
        if (mMovieList.getResults().size() == 0)
            return null;
        return mMovieList.getResults().get(atPosition);
    }

    @Override
    public int getTopPages() {
        return mMovieList.getTotalPages();
    }

    @Override
    public int getTotalMoviesCount() {
        return mMovieList.getResults().size();
    }

    @Override
    public void clearMoviesList() {
        mMovieList.clear();
    }

    @Override
    public ArrayList<Video> getVideoList() {
        return mMovieDetails.getVideos().getResults();
    }

    @Override
    public void getPopularMovies(int pageNoToLoad, @NonNull final MoviesRepositoryCallback pCallback, boolean isRefresh) {
        checkForNull();
        if (isRefresh) {
            CacheImpl.INSTANCE.removeAllMovieList(MoviesServiceApi.POPULAR_SORT);
        }
        showProgressDialog(mContext.getString(R.string.load_popular_movies), isRefresh);
        getMoviesList(MoviesServiceApi.POPULAR_SORT, pageNoToLoad, pCallback);
    }

    @Override
    public void getTopRatedMovies(int pageNoToLoad, @NonNull final MoviesRepositoryCallback pCallback, boolean isRefresh) {
        checkForNull();
        if (isRefresh) {
            CacheImpl.INSTANCE.removeAllMovieList(MoviesServiceApi.TOP_RATED_SORT);
        }
        showProgressDialog(mContext.getString(R.string.load_top_rated_movies), isRefresh);
        getMoviesList(MoviesServiceApi.TOP_RATED_SORT, pageNoToLoad, pCallback);
    }

    @Override
    public void getMovieDetails(int movieId, @NonNull final MoviesRepositoryCallback pCallback) {
        checkForNull();
        showProgressDialog(mContext.getString(R.string.loading_movie), false);
        isLoading = true;
        mCompositeSubscription.add(mMoviesServiceInterface.getMovieDetails(movieId, mApiKey, new MoviesServiceInterface.MoviesServiceCallback<MovieDetails>() {
            @Override
            public void onSuccess(MovieDetails movies) {
                mMovieDetails = movies;
                pCallback.onSuccess();
                isLoading = false;
                dismissProgressDialog();
            }

            @Override
            public void onFailure(String message) {
                pCallback.onFailure(message);
                isLoading = false;
                dismissProgressDialog();
            }
        }));
    }

    @Override
    public MovieDetails getMovieDetails() {
        return mMovieDetails;
    }

    private void getMoviesList(@MoviesServiceApi.MovieSortTypes String movieSort, int pageNoToLoad, @NonNull final MoviesRepositoryCallback pCallback) {
        isLoading = true;
        mCompositeSubscription.add(
                mMoviesServiceInterface.getMoviesList(movieSort, mApiKey, pageNoToLoad, new MoviesServiceInterface.MoviesServiceCallback<MovieList>() {
                    @Override
                    public void onSuccess(MovieList movies) {
                        mMovieList.updateMovieList(movies);
                        pCallback.onSuccess();
                        isLoading = false;
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(String message) {
                        pCallback.onFailure(message);
                        isLoading = false;
                        dismissProgressDialog();
                    }
                }));
    }

    private void showProgressDialog(String pMessage, boolean isRefresh) {
        if (isRefresh)
            return;
        if (mMovieList.getPage() > PAGE_FROM_WHICH_LOADING_NOT_SHOWN)
            return;
        if (mProgressDialog == null)
            return;
        mProgressDialog.setMessage(pMessage);
        mProgressDialog.show();
    }

    private void dismissProgressDialog() {
        if (mMovieList.getPage() > PAGE_FROM_WHICH_DISMISS_NOT_CALLED)
            return;
        if (mProgressDialog != null || mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void checkForNull() {
        if (mCompositeSubscription == null || mContext == null || mProgressDialog == null)
            throw new NullPointerException("Invoke init method of repository");
    }
}
