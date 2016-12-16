package com.fabinpaul.project_2_popularmovies.features.moviesdetail.logic;

import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.Video;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 12/5/2016 12:33 PM.
 */

public interface MovieDetailsContract {

    interface View {
        void openYouTubeVideo(String url);
    }

    interface UserInteractions {
        void onMovieVideoClick(Video video);

        void setMovieAsFavourite(boolean isFav);
    }
}
