package com.fabinpaul.project_1_popularmovies.framework.repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.fabinpaul.project_1_popularmovies.R;
import com.fabinpaul.project_1_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_1_popularmovies.features.movieshome.data.MovieList;
import com.fabinpaul.project_1_popularmovies.framework.network.MoviesServiceInterface;

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

    private ProgressDialog mProgressDialog;
    private Context mContext;

    public MoviesRepositoryImpl(Context pContext, MoviesServiceInterface pMoviesServiceInterface) {
        mContext = pContext;
        mMoviesServiceInterface = pMoviesServiceInterface;
        mCompositeSubscription = new CompositeSubscription();
        mMovieList = new MovieList();

        mProgressDialog = new ProgressDialog(pContext);
        mProgressDialog.setIndeterminate(true);
    }

    @Override
    public void onDestroy() {
        mCompositeSubscription.unsubscribe();
        mContext = null;
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
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
    public void getPopularMovies(int pageNoToLoad, @NonNull final MoviesRepositoryCallback<MovieList> pCallback) {
        String apiKey = mContext.getString(R.string.api_key);
        showProgressDialog(mContext.getString(R.string.load_popular_movies));
        mCompositeSubscription.add(
                mMoviesServiceInterface.getPopularMovies(apiKey, pageNoToLoad, new MoviesServiceInterface.MoviesServiceCallback<MovieList>() {
                    @Override
                    public void onSuccess(MovieList movies) {
                        mMovieList.updateMovieList(movies);
                        pCallback.onSuccess(movies);
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(String message) {
                        pCallback.onFailure(message);
                        dismissProgressDialog();
                    }
                }));
    }

    @Override
    public void getTopRatedMovies(int pageNoToLoad, @NonNull final MoviesRepositoryCallback<MovieList> pCallback) {
        String apiKey = mContext.getString(R.string.api_key);
        showProgressDialog(mContext.getString(R.string.load_top_rated_movies));
        mCompositeSubscription.add(
                mMoviesServiceInterface.getTopRatedMovies(apiKey, pageNoToLoad, new MoviesServiceInterface.MoviesServiceCallback<MovieList>() {
                    @Override
                    public void onSuccess(MovieList movies) {
                        mMovieList.updateMovieList(movies);
                        pCallback.onSuccess(movies);
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(String message) {
                        pCallback.onFailure(message);
                        dismissProgressDialog();
                    }
                }));
    }

    private void showProgressDialog(String pMessage) {
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
