package com.fabinpaul.project_2_popularmovies.features.moviesdetail.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fabinpaul.project_2_popularmovies.R;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_2_popularmovies.framework.repository.MoviesRepository;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MoviesDetailActivity extends AppCompatActivity {

    private Unbinder mUnBinder;

/*    @BindView(R.id.movies_detail_background_imgvw)
    ProportionalImageView mBackdropImgVw;

    @BindView(R.id.movie_detail_collapsing_toolbar)
    CollapsingToolbarLayout mMovieCollapsingToolbarLayout;

    @BindView(R.id.movie_fav_fab)
    FloatingActionButton mFavButton;*/

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);
        mUnBinder = ButterKnife.bind(this);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);*/

        mFragment = getFragmentManager().findFragmentById(R.id.movie_detail_fragment_container);
        Movie movieDetails = null;
        if (getIntent() != null && getIntent().getParcelableExtra(MoviesRepository.MOVIE_EXTRA) != null) {
            movieDetails = getIntent().getParcelableExtra(MoviesRepository.MOVIE_EXTRA);
        }

        /*if (movieDetails != null) {
            Picasso.with(this)
                    .load(MoviesServiceImpl.getBackDropPath(MoviesServiceApi.W500, movieDetails.getBackdropPath()))
                    .into(mBackdropImgVw);
        }*/
        if (mFragment == null) {
            mFragment = MoviesDetailFragment.newInstance(movieDetails);
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.movie_detail_fragment_container, mFragment)
                    .commit();
        }

        /*mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MoviesDetailFragment)mFragment).setMovieAsFavourite();
            }
        });*/

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void startActivity(Activity pActivity, Movie pMovie) {
        Intent intent = new Intent(pActivity, MoviesDetailActivity.class);
        intent.putExtra(MoviesRepository.MOVIE_EXTRA, pMovie);
        pActivity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }
}
