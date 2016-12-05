package com.fabinpaul.project_2_popularmovies.framework.repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.fabinpaul.project_2_popularmovies.R;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.MovieList;
import com.fabinpaul.project_2_popularmovies.framework.network.CacheImpl;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceApi;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceInterface;

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
    private String mApiKey;
    private boolean isLoading;

    private ProgressDialog mProgressDialog;
    private Context mContext;

    public MoviesRepositoryImpl(Context pContext, MoviesServiceInterface pMoviesServiceInterface) {
        mContext = pContext;
        mMoviesServiceInterface = pMoviesServiceInterface;
        mCompositeSubscription = new CompositeSubscription();
        mMovieList = new MovieList();

        mApiKey = mContext.getString(R.string.api_key);
        mProgressDialog = new ProgressDialog(pContext);
        mProgressDialog.setIndeterminate(true);
    }

    @Override
    public void onDestroy() {
        mCompositeSubscription.unsubscribe();
        mContext = null;
        mProgressDialog = null;
    }

    @Override
    public void onStop() {
        if (mProgressDialog.isShowing()) {
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
    public Movie getMovie(int atPosition) {
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
    public void getPopularMovies(int pageNoToLoad, @NonNull final MoviesRepositoryCallback<MovieList> pCallback, boolean isRefresh) {
        if (isRefresh) {
            CacheImpl.INSTANCE.removeAllMovieList(MoviesServiceApi.POPULAR_SORT);
        }
        showProgressDialog(mContext.getString(R.string.load_popular_movies), isRefresh);
        getMoviesList(MoviesServiceApi.POPULAR_SORT, pageNoToLoad, pCallback);
    }

    @Override
    public void getTopRatedMovies(int pageNoToLoad, @NonNull final MoviesRepositoryCallback<MovieList> pCallback, boolean isRefresh) {
        if (isRefresh) {
            CacheImpl.INSTANCE.removeAllMovieList(MoviesServiceApi.TOP_RATED_SORT);
        }
        showProgressDialog(mContext.getString(R.string.load_top_rated_movies), isRefresh);
        getMoviesList(MoviesServiceApi.TOP_RATED_SORT, pageNoToLoad, pCallback);
    }

    @Override
    public void getMovieDetails(int movieId, final MoviesRepositoryCallback<MovieDetails> pCallback) {
        showProgressDialog(mContext.getString(R.string.loading_movie), false);
        isLoading = true;
        mCompositeSubscription.add(mMoviesServiceInterface.getMovieDetails(movieId, mApiKey, new MoviesServiceInterface.MoviesServiceCallback<MovieDetails>() {
            @Override
            public void onSuccess(MovieDetails movies) {
                pCallback.onSuccess(movies);
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

    private void getMoviesList(@MoviesServiceApi.MovieSortTypes String movieSort, int pageNoToLoad, @NonNull final MoviesRepositoryCallback<MovieList> pCallback) {
        isLoading = true;
        mCompositeSubscription.add(
                mMoviesServiceInterface.getMoviesList(movieSort, mApiKey, pageNoToLoad, new MoviesServiceInterface.MoviesServiceCallback<MovieList>() {
                    @Override
                    public void onSuccess(MovieList movies) {
                        mMovieList.updateMovieList(movies);
                        pCallback.onSuccess(movies);
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
        mProgressDialog.setMessage(pMessage);
        mProgressDialog.show();
    }

    private void dismissProgressDialog() {
        if (mMovieList.getPage() > PAGE_FROM_WHICH_DISMISS_NOT_CALLED)
            return;
        mProgressDialog.dismiss();
    }
}
