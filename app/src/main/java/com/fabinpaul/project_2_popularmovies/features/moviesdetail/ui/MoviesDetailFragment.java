package com.fabinpaul.project_1_popularmovies.features.moviesdetail.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fabinpaul.project_1_popularmovies.R;
import com.fabinpaul.project_1_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_1_popularmovies.framework.network.MoviesServiceApi;
import com.fabinpaul.project_1_popularmovies.framework.network.MoviesServiceImpl;
import com.fabinpaul.project_1_popularmovies.framework.ui.ProportionalImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesDetailFragment extends Fragment {

    private static final String MOVIE_EXTRA = "com.fabinpaul.project_1_popularmovies.MovieExtra";

    private Unbinder mUnBinder;

    @BindView(R.id.movie_detail_overview_txtvw)
    TextView mMovieOverviewTxtVw;
    @BindView(R.id.movie_detail_rating_bar)
    RatingBar mMovieRatingBar;
    @BindView(R.id.movies_detail_poster_imgvw)
    ProportionalImageView mMoviePosterImgVw;
    @BindView(R.id.movies_detail_title_txtvw)
    TextView mMovieTitleTxtVw;
    @BindView(R.id.movies_detail_release_date_txtvw)
    TextView mReleaseDateTxtVw;

    private Movie mMovie;

    public static MoviesDetailFragment newInstance(Movie pMovie) {
        MoviesDetailFragment fragment = new MoviesDetailFragment();
        Bundle movieExtra = new Bundle();
        movieExtra.putParcelable(MOVIE_EXTRA, pMovie);
        fragment.setArguments(movieExtra);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getParcelable(MOVIE_EXTRA) != null) {
            mMovie = getArguments().getParcelable(MOVIE_EXTRA);
        }
    }

    public MoviesDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_detail, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        if (mMovie != null) {
            Picasso.with(getActivity())
                    .load(MoviesServiceImpl.getPosterPath(MoviesServiceApi.W154, mMovie.getPoster_path()))
                    .into(mMoviePosterImgVw);
            mMovieTitleTxtVw.setText(mMovie.getOriginalTitle());
            try {
                mReleaseDateTxtVw.setText(mMovie.getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mMovieOverviewTxtVw.setText(mMovie.getOverview());
            mMovieRatingBar.setRating(mMovie.getVoteAverage());
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }
}
