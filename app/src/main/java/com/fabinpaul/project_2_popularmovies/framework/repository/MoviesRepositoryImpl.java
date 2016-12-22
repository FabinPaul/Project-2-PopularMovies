package com.fabinpaul.project_2_popularmovies.framework.repository;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.fabinpaul.project_2_popularmovies.BuildConfig;
import com.fabinpaul.project_2_popularmovies.R;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.Video;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.MovieList;
import com.fabinpaul.project_2_popularmovies.features.movieshome.logic.MoviesListContract;
import com.fabinpaul.project_2_popularmovies.framework.data.MoviesDBContract;
import com.fabinpaul.project_2_popularmovies.framework.network.Cache;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceApi;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceInterface;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Fabin Paul on 11/6/2016 5:06 PM.
 */

public class MoviesRepositoryImpl implements MoviesRepository, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PAGE_FROM_WHICH_LOADING_NOT_SHOWN = 0;
    private static final int PAGE_FROM_WHICH_DISMISS_NOT_CALLED = 1;

    private static final int GET_FAV_LIST = 0;
    private static final int GET_FAV_ID_LIST = 1;


    private final MoviesServiceInterface mMoviesServiceInterface;
    private CompositeSubscription mCompositeSubscription;
    private MovieList mMovieList;
    private MovieList mFavMovieList;
    private MovieDetails mMovieDetails;
    private ArrayList<Integer> mFavMovieIdList;
    private String mApiKey;
    private boolean isLoading;

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private MoviesLoaderCallback mMoviesLoaderCallback;
    private int mCurrentSort;

    public MoviesRepositoryImpl(Context pContext, MoviesServiceInterface pMoviesServiceInterface) {
        if (!(pContext instanceof Activity)) {
            throw new InvalidParameterException("Pass activity context");
        }
        mContext = pContext;
        mMoviesServiceInterface = pMoviesServiceInterface;
        mMovieList = new MovieList();
        mMovieDetails = new MovieDetails();
        mFavMovieList = new MovieList();
        mFavMovieIdList = new ArrayList<>();

        mCompositeSubscription = new CompositeSubscription();
        mProgressDialog = new ProgressDialog(pContext);
        mProgressDialog.setIndeterminate(true);

        mApiKey = BuildConfig.API_KEY;
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
        mCurrentSort = MoviesListContract.POPULAR_MOVIE;
        if (isRefresh) {
            Cache.INSTANCE.removeAllMovieList(MoviesServiceApi.POPULAR_SORT);
        }
        showProgressDialog(mContext.getString(R.string.load_popular_movies), isRefresh);
        getMoviesList(MoviesServiceApi.POPULAR_SORT, pageNoToLoad, pCallback);
    }

    @Override
    public void getTopRatedMovies(int pageNoToLoad, @NonNull final MoviesRepositoryCallback pCallback, boolean isRefresh) {
        checkForNull();
        mCurrentSort = MoviesListContract.TOP_RATED_MOVIE;
        if (isRefresh) {
            Cache.INSTANCE.removeAllMovieList(MoviesServiceApi.TOP_RATED_SORT);
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

    @Override
    public void setMovieAsFavourite(boolean isFav) {
        if (isFav) {
            mContext.getContentResolver().insert(MoviesDBContract.MoviesTB.CONTENT_URI, getMovieTBContentValues(mMovieDetails));
            mContext.getContentResolver().insert(MoviesDBContract.FavouriteMoviesTB.CONTENT_URI, getFavMovieTBContentValues(mMovieDetails.getId()));
        } else {
            mContext.getContentResolver().delete(MoviesDBContract.FavouriteMoviesTB.CONTENT_URI,
                    MoviesDBContract.FavouriteMoviesTB.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(mMovieDetails.getId())});
        }
        mMovieDetails.setFavourite(isFav);
    }

    @Override
    public void getFavouriteMovies(@NonNull MoviesLoaderCallback pCallback) {
        mMoviesLoaderCallback = pCallback;
        mCurrentSort = MoviesListContract.FAVOURITE_MOVIE;
        mMovieList.clear();
        mMovieList.updateMovieList(mFavMovieList);
        ((Activity) mContext).getLoaderManager().initLoader(GET_FAV_LIST, null, this);
    }

    @Override
    public void loadFavouriteMovieId() {
        ((Activity) mContext).getLoaderManager().initLoader(GET_FAV_ID_LIST, null, this);
    }

    @Override
    public boolean isFavouriteMovie(int pMovieId) {
        return mFavMovieIdList != null && mFavMovieIdList.contains(pMovieId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case GET_FAV_LIST:
                return new CursorLoader(mContext,
                        MoviesDBContract.FavouriteMoviesTB.buildFavMoveListUri(),
                        null, null,
                        null, null);
            case GET_FAV_ID_LIST:
                return new CursorLoader(mContext,
                        MoviesDBContract.FavouriteMoviesTB.CONTENT_URI,
                        null, null,
                        null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case GET_FAV_LIST:
                ArrayList<Movie> movies = getMovieListFromCursor(data);
                mFavMovieList.clear();
                MovieList movieList = new MovieList();
                movieList.setPage(1);
                movieList.setResults(movies);
                movieList.setTotal_pages(1);
                movieList.setTotal_results(movies.size());
                mFavMovieList.updateMovieList(movieList);
                if (mCurrentSort == MoviesListContract.FAVOURITE_MOVIE) {
                    mMovieList.clear();
                    mMovieList.updateMovieList(mFavMovieList);
                    if (mMoviesLoaderCallback != null)
                        mMoviesLoaderCallback.onSuccess();
                }
                break;
            case GET_FAV_ID_LIST:
                mFavMovieIdList = getMovieIdFromCursor(data);
                break;
            default:
                break;
        }
    }

    private ArrayList<Integer> getMovieIdFromCursor(Cursor data) {
        ArrayList<Integer> movieIdList = new ArrayList<>();
        if (data != null && data.moveToFirst()) {
            do {
                movieIdList.add(data.getInt(data.getColumnIndex(MoviesDBContract.FavouriteMoviesTB.COLUMN_MOVIE_ID)));
            } while (data.moveToNext());
        }
        return movieIdList;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mMoviesLoaderCallback != null && mCurrentSort == MoviesListContract.FAVOURITE_MOVIE)
            mMoviesLoaderCallback.onReset();
    }

    private ArrayList<Movie> getMovieListFromCursor(Cursor data) {
        ArrayList<Movie> movieList = new ArrayList<>();
        if (data != null && data.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(data.getInt(data.getColumnIndex(MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID)));
                movie.setBackdrop_path(data.getString(data.getColumnIndex(MoviesDBContract.MoviesTB.COLUMN_BACKGROUND)));
                movie.setOriginal_language(data.getString(data.getColumnIndex(MoviesDBContract.MoviesTB.COLUMN_LANGUAGE)));
                movie.setOriginal_title(data.getString(data.getColumnIndex(MoviesDBContract.MoviesTB.COLUMN_TITLE)));
                movie.setOverview(data.getString(data.getColumnIndex(MoviesDBContract.MoviesTB.COLUMN_OVERVIEW)));
                movie.setPopularity(data.getFloat(data.getColumnIndex(MoviesDBContract.MoviesTB.COLUMN_POPULARITY)));
                movie.setPoster_path(data.getString(data.getColumnIndex(MoviesDBContract.MoviesTB.COLUMN_POSTER)));
                movie.setRelease_date(data.getString(data.getColumnIndex(MoviesDBContract.MoviesTB.COLUMN_RELEASE_DATE)));
                movie.setVote_average(data.getFloat(data.getColumnIndex(MoviesDBContract.MoviesTB.COLUMN_VOTE_AVERAGE)));
                movie.setVote_count(data.getInt(data.getColumnIndex(MoviesDBContract.MoviesTB.COLUMN_VOTE_COUNT)));
                movie.setFavourite(true);
                movieList.add(movie);
            } while (data.moveToNext());
        }
        return movieList;
    }

    private ContentValues getMovieTBContentValues(Movie pMovie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_MOVIE_ID, pMovie.getId());
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_BACKGROUND, pMovie.getBackdropPath());
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_LANGUAGE, pMovie.getOriginalLanguage());
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_OVERVIEW, pMovie.getOverview());
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_POPULARITY, pMovie.getPopularity());
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_POSTER, pMovie.getPoster_path());
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_RELEASE_DATE, pMovie.getReleaseDateString());
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_TITLE, pMovie.getTitle());
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_VOTE_AVERAGE, pMovie.getVoteAverage());
        contentValues.put(MoviesDBContract.MoviesTB.COLUMN_VOTE_COUNT, pMovie.getVoteCount());
        return contentValues;
    }

    private ContentValues getFavMovieTBContentValues(int pMovieId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesDBContract.FavouriteMoviesTB.COLUMN_MOVIE_ID, pMovieId);
        return contentValues;
    }

    private void getMoviesList(@MoviesServiceApi.MovieSortTypes String movieSort, int pageNoToLoad, @NonNull final MoviesRepositoryCallback pCallback) {
        isLoading = true;
        mCompositeSubscription.add(
                mMoviesServiceInterface.getMoviesList(movieSort, mApiKey, pageNoToLoad, new MoviesServiceInterface.MoviesServiceCallback<MovieList>() {
                    @Override
                    public void onSuccess(MovieList movies) {
                        mMovieList.updateMovieList(movies);
                        if (pCallback != null)
                            pCallback.onSuccess();
                        isLoading = false;
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(String message) {
                        if (pCallback != null)
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
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void checkForNull() {
        if (mCompositeSubscription == null || mContext == null || mProgressDialog == null)
            throw new NullPointerException("Invoke init method of repository");
    }
}
