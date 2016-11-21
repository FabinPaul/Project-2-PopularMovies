package com.fabinpaul.project_1_popularmovies.features.moviesdetail.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fabinpaul.project_1_popularmovies.R;
import com.fabinpaul.project_1_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_1_popularmovies.framework.network.MoviesServiceApi;
import com.fabinpaul.project_1_popularmovies.framework.network.MoviesServiceImpl;
import com.fabinpaul.project_1_popularmovies.framework.ui.ProportionalImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MoviesDetailActivity extends AppCompatActivity {

    private static final String MOVIE_EXTRA = "com.fabinpaul.project_1_popularmovies.MovieExtra";
    private Unbinder mUnBinder;

    @BindView(R.id.movies_detail_background_imgvw)
    ProportionalImageView mBackdropImgVw;

    @BindView(R.id.movie_detail_collapsing_toolbar)
    CollapsingToolbarLayout mMovieCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);
        mUnBinder = ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.movie_detail_fragment_container);
        Movie movieDetails = null;
        if (getIntent() != null && getIntent().getParcelableExtra(MOVIE_EXTRA) != null) {
            movieDetails = getIntent().getParcelableExtra(MOVIE_EXTRA);
        }

        if (movieDetails != null) {
            Picasso.with(this)
                    .load(MoviesServiceImpl.getBackDropPath(MoviesServiceApi.W500, movieDetails.getBackdropPath()))
                    .into(mBackdropImgVw);
        }
        if (fragment == null) {
            fragment = MoviesDetailFragment.newInstance(movieDetails);
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.movie_detail_fragment_container, fragment)
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void startActivity(Activity pActivity, Movie pMovie) {
        Intent intent = new Intent(pActivity, MoviesDetailActivity.class);
        intent.putExtra(MOVIE_EXTRA, pMovie);
        pActivity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }
}
