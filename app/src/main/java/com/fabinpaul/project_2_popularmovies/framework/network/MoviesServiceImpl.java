package com.fabinpaul.project_2_popularmovies.framework.network;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
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
    private static final String ADDITIONAL_RESPONSE = "videos,reviews";

    private Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MoviesServiceApi.BASE_URL)
            .build();

    @Override
    public Subscription getMoviesList(@MoviesServiceApi.MovieSortTypes final String moviesSort,
                                      String apiKey, final int page,
                                      final MoviesServiceCallback<MovieList> pCallback) {
        if (checkIfNull(apiKey, pCallback)) {
            throw new NullPointerException("Service Callback cannot be null for " + moviesSort);
        }
        Observable<MovieList> movieListObservable;
        if ((CacheImpl.INSTANCE.getFromCache(moviesSort + page) != null)) {
            MovieList movieList = (MovieList) CacheImpl.INSTANCE.getFromCache(moviesSort + page);
            movieListObservable = Observable.just(movieList);
        } else {
            movieListObservable = retrofit.create(MoviesServiceApi.class)
                    .getMoviesList(moviesSort, apiKey, page);
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
                        Log.e(TAG, "Error fetching " + moviesSort + " movies " + e.getMessage());
                        pCallback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieList pMovieList) {
                        CacheImpl.INSTANCE.addToCache(moviesSort + page, pMovieList);
                        pCallback.onSuccess(pMovieList);
                    }
                });
    }

    @Override
    public Subscription getMovieDetails(final int pMovieId, String apiKey, @NonNull final MoviesServiceCallback<MovieDetails> pCallback) {
        if (checkIfNull(pMovieId, apiKey, pCallback)) {
            throw new NullPointerException("Service Callback cannot be null for movieId " + pMovieId);
        }
        Observable<MovieDetails> movieDetailsObservable;
        if ((CacheImpl.INSTANCE.getFromCache(pMovieId) != null)) {
            MovieDetails movieDetails = (MovieDetails) CacheImpl.INSTANCE.getFromCache(pMovieId);
            movieDetailsObservable = Observable.just(movieDetails);
        } else {
            movieDetailsObservable = retrofit.create(MoviesServiceApi.class)
                    .getMovieDetails(pMovieId, apiKey, ADDITIONAL_RESPONSE);
        }
        return movieDetailsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<MovieDetails>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Movie details fetched");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error fetching movie " + pMovieId);
                        pCallback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieDetails movieDetails) {
                        CacheImpl.INSTANCE.addToCache(pMovieId, movieDetails);
                        pCallback.onSuccess(movieDetails);
                    }
                });
    }

    private boolean checkIfNull(String apiKey, MoviesServiceCallback<MovieList> pCallback) {
        return TextUtils.isEmpty(apiKey) && pCallback == null;
    }

    private boolean checkIfNull(int pMovieId, String apiKey, MoviesServiceCallback<MovieDetails> pCallback) {
        return pMovieId > 0 && TextUtils.isEmpty(apiKey) && pCallback == null;
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
