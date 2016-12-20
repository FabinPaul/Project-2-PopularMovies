package com.fabinpaul.project_2_popularmovies.features.moviesdetail.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_2_popularmovies.framework.repository.MoviesRepository;

public class MoviesDetailActivity extends AppCompatActivity {

//    private Unbinder mUnBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movies_detail);
//        mUnBinder = ButterKnife.bind(this);

        Movie movieDetails = null;
        if (getIntent() != null && getIntent().getParcelableExtra(MoviesRepository.MOVIE_EXTRA) != null) {
            movieDetails = getIntent().getParcelableExtra(MoviesRepository.MOVIE_EXTRA);
        }

        if (savedInstanceState  == null) {
            MoviesDetailFragment fragment = MoviesDetailFragment.newInstance(movieDetails);
            getFragmentManager()
                    .beginTransaction()
//                    .add(R.id.movie_detail_fragment_container, fragment)
                    .add(android.R.id.content, fragment)
                    .commit();
        }
    }

    public static void startActivity(Activity pActivity, Movie pMovie) {
        Intent intent = new Intent(pActivity, MoviesDetailActivity.class);
        intent.putExtra(MoviesRepository.MOVIE_EXTRA, pMovie);
        pActivity.startActivity(intent);
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }*/
}
