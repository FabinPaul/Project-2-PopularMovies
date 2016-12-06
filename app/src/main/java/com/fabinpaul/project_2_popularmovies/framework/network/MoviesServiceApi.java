package com.fabinpaul.project_2_popularmovies.framework.network;

import android.support.annotation.StringDef;

import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.MovieList;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Fabin Paul on 11/6/2016 1:55 PM.
 */

public interface MoviesServiceApi {

    String BASE_URL = "http://api.themoviedb.org/3/";

    String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w300/";

    String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/mqdefault.jpg";

    String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    String W300 = "w300", W700 = "w700", W1280 = "w1280";
    String W92 = "w92", W154 = "w154", W185 = "w185", W342 = "w342", W500 = "w500";

    String TOP_RATED_SORT = "top_rated", POPULAR_SORT = "popular";

    @GET("movie/{sort}")
    Observable<MovieList> getMoviesList(@Path("sort") @MovieSortTypes String movieSort, @Query("api_key") String apiKey, @Query("page") int page);


    @GET("movie/{movie_id}")
    Observable<MovieDetails> getMovieDetails(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("append_to_response") String additionalResponses);


    @Retention(RetentionPolicy.SOURCE)
    // Enumerate valid values for this interface
    @StringDef({W300, W342, W500, W700, W1280})
    // Create an interface for validating int types
    public @interface BackdropImgSize {
    }

    @Retention(RetentionPolicy.SOURCE)
    // Enumerate valid values for this interface
    @StringDef({W92, W154, W185, W300, W342, W500})
            // Create an interface for validating int types
    @interface PosterImgSize {
    }

    @Retention(RetentionPolicy.SOURCE)
    // Enumerate valid values for this interface
    @StringDef({TOP_RATED_SORT, POPULAR_SORT})
            // Create an interface for validating int types
    @interface MovieSortTypes {
    }
}
