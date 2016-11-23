package com.fabinpaul.project_2_popularmovies.framework.network;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.fabinpaul.project_2_popularmovies.features.movieshome.data.MovieList;

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

    private Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MoviesServiceApi.BASE_URL)
            .build();

    @Override
    public Subscription getMoviesList(@MoviesServiceApi.MovieSortTypes final String moviesSort, String apiKey, final int page, final MoviesServiceCallback<MovieList> pCallback) {
        if (checkIfNull(apiKey, pCallback)) {
            throw new NullPointerException("Service Callback cannot be null for "+moviesSort);
        }
        Observable<MovieList> movieListObservable;
        if (CacheImpl.INSTANCE.getFromCache(moviesSort + page) != null) {
            MovieList movieList = (MovieList) CacheImpl.INSTANCE.getFromCache(moviesSort + page);
            movieListObservable = Observable.just(movieList);
        } else {
            movieListObservable = retrofit.create(MoviesServiceApi.class)
                    .getMoviesList(moviesSort,apiKey, page);
        }
        return movieListObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<MovieList>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Movies list fetched");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error fetching Top Rated movies");
                        pCallback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieList pMovieList) {
                        CacheImpl.INSTANCE.addToCache(moviesSort + page, pMovieList);
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
