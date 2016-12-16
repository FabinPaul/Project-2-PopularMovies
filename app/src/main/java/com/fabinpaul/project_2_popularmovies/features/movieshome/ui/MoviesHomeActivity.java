package com.fabinpaul.project_2_popularmovies.features.movieshome.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fabinpaul.project_2_popularmovies.R;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.ui.MoviesDetailActivity;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.ui.MoviesDetailFragment;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;

public class MoviesHomeActivity extends AppCompatActivity implements MoviesHomeFragment.Callback {

    private static final String DETAIL_FRAGMENT = "com.fabinpaul.project_2_popularmovies.MoviesDetailFragment";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_fragment_container) != null) {
            mTwoPane = true;
        }

        MoviesHomeFragment fragment = ((MoviesHomeFragment) getFragmentManager().findFragmentById(R.id.movie_list_fragment));
        fragment.setAsTwoPaneLayout(mTwoPane);
    }

    @Override
    public void onItemSelected(@NonNull Movie pMovie) {
        if (mTwoPane) {
            MoviesDetailFragment fragment = MoviesDetailFragment.newInstance(pMovie);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movie_detail_fragment_container, fragment, DETAIL_FRAGMENT)
                    .commit();
        } else
            MoviesDetailActivity.startActivity(this, pMovie);
    }
}
