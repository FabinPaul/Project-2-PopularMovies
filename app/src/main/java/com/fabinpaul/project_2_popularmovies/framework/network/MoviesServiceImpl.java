package com.fabinpaul.project_1_popularmovies.framework.network;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.fabinpaul.project_1_popularmovies.features.movieshome.data.MovieList;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Fabin Paul on 11/6/2016 2:17 PM.
 */

public class MoviesServiceImpl implements MoviesServiceInterface {

    private static final String TAG = MoviesServiceImpl.class.getSimpleName();

    private static final String POPULAR_MOVIES_REQUEST_TAG = "Popular_Movies_Request";
    private static final String TOP_RATED_MOVIES_REQUEST_TAG = "Top_Rated_Movies_Request";

    private Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MoviesServiceApi.BASE_URL)
            .build();

    @Override
    public Subscription getPopularMovies(@NonNull String apiKey, final int page, @NonNull final MoviesServiceCallback<MovieList> pCallback) {
        if (checkIfNull(apiKey, pCallback)) {
            throw new NullPointerException("Popular Movies Service Callback cannot be null");
        }
        Observable<MovieList> movieListObservable;
        if (CacheImpl.INSTANCE.getFromCache(POPULAR_MOVIES_REQUEST_TAG + page) != null) {
            MovieList movieList = (MovieList) CacheImpl.INSTANCE.getFromCache(POPULAR_MOVIES_REQUEST_TAG + page);
            movieListObservable = Observable.just(movieList);
        } else {
            movieListObservable = retrofit.create(MoviesServiceApi.class)
                    .getPopularMovies(apiKey, page);
        }
        return movieListObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<MovieList>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Popular movies fetch complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error fetching popular movies");
                        pCallback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieList pMovieList) {
                        CacheImpl.INSTANCE.addToCache(POPULAR_MOVIES_REQUEST_TAG + page, pMovieList);
                        pCallback.onSuccess(pMovieList);
                    }
                });
    }

    @Override
    public Subscription getTopRatedMovies(@NonNull String apiKey, final int page, @NonNull final MoviesServiceCallback<MovieList> pCallback) {
        if (checkIfNull(apiKey, pCallback)) {
            throw new NullPointerException("Top Rated Service Callback cannot be null");
        }
        Observable<MovieList> movieListObservable;
        if (CacheImpl.INSTANCE.getFromCache(TOP_RATED_MOVIES_REQUEST_TAG + page) != null) {
            MovieList movieList = (MovieList) CacheImpl.INSTANCE.getFromCache(TOP_RATED_MOVIES_REQUEST_TAG + page);
            movieListObservable = Observable.just(movieList);
        } else {
            movieListObservable = retrofit.create(MoviesServiceApi.class)
                    .getTopRatedMovies(apiKey, page);
        }
        return movieListObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<MovieList>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Top Rated movies fetch complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error fetching Top Rated movies");
                        pCallback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieList pMovieList) {
                        CacheImpl.INSTANCE.addToCache(TOP_RATED_MOVIES_REQUEST_TAG + page, pMovieList);
                        pCallback.onSuccess(pMovieList);
                    }
                });
    }

    private boolean checkIfNull(String apiKey, MoviesServiceCallback<MovieList> pCallback) {
        return TextUtils.isEmpty(apiKey) && pCallback == null;
    }

    public static String getBackDropPath(@MoviesServiceApi.BackdropImgSize String backdropImgSize, String imageName) {
        return new Uri.Builder()
                .scheme("https")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath(backdropImgSize)
                .toString() + imageName;
    }

    public static String getPosterPath(@MoviesServiceApi.PosterImgSize String posterImgSize, String imageName) {
        return new Uri.Builder()
                .scheme("https")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath(posterImgSize)
                .toString() + imageName;
    }
}
