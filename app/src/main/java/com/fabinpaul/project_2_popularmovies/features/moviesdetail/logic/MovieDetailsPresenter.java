package com.fabinpaul.project_2_popularmovies.features.moviesdetail.logic;

import android.support.annotation.NonNull;

import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.Video;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceApi;
import com.fabinpaul.project_2_popularmovies.framework.repository.MoviesRepository;

import java.util.ArrayList;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 12/5/2016 12:36 PM.
 */

public class MovieDetailsPresenter implements MovieDetailsContract.UserInteractions {

    private final MovieDetailsContract.View mView;
    private final MoviesRepository mMoviesRepository;

    public MovieDetailsPresenter(MovieDetailsContract.View view, MoviesRepository moviesRepository) {
        mView = view;
        mMoviesRepository = moviesRepository;
    }

    public int getVideoCount() {
        if (mMoviesRepository.getVideoList() == null)
            return 0;
        return mMoviesRepository.getVideoList().size();
    }

    public ArrayList<Video> getVideoList() {
        return mMoviesRepository.getVideoList();
    }

    public Video getVideoAtPosition(int position) {
        return mMoviesRepository.getVideoList().get(position);
    }

    public void getMoviesDetails(int pMovieId, @NonNull MoviesRepository.MoviesRepositoryCallback pCallback) {
        mMoviesRepository.getMovieDetails(pMovieId, pCallback);
    }

    @Override
    public void onMovieVideoClick(Video video) {
        if (video != null && video.isYoutubeVideo()) {
            mView.openYouTubeVideo(MoviesServiceApi.YOUTUBE_URL + video.getKey());
        }
    }

    @Override
    public void setMovieAsFavourite(boolean isFav) {
        mMoviesRepository.setMovieAsFavourite(isFav);
    }

    public MovieDetails getMovieDetails() {
        return mMoviesRepository.getMovieDetails();
    }
}
