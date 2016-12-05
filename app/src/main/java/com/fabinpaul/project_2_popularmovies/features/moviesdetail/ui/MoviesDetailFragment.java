package com.fabinpaul.project_2_popularmovies.features.moviesdetail.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fabinpaul.project_2_popularmovies.R;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.Genres;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.MovieDetails;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.logic.MovieDetailsContract;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.logic.MovieDetailsPresenter;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceApi;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceImpl;
import com.fabinpaul.project_2_popularmovies.framework.repository.MoviesRepository;
import com.fabinpaul.project_2_popularmovies.framework.repository.MoviesRepositoryImpl;
import com.fabinpaul.project_2_popularmovies.framework.ui.ProportionalImageView;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesDetailFragment extends Fragment implements MovieDetailsContract.View {

    private static final String MOVIE_EXTRA = "com.fabinpaul.project_2_popularmovies.MovieExtra";
    private static final String TAG = MoviesDetailFragment.class.getSimpleName();

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
    @BindView(R.id.movies_detail_genre_list)
    FlexboxLayout mGenreContainer;

    private MovieDetails mMovieDetails;
    private MovieDetailsPresenter mDetailsPresenter;

    public MoviesDetailFragment() {
    }

    public static MoviesDetailFragment newInstance(Movie pMovie) {
        MoviesDetailFragment fragment = new MoviesDetailFragment();
        Bundle movieExtra = new Bundle();
        movieExtra.putParcelable(MOVIE_EXTRA, pMovie);
        fragment.setArguments(movieExtra);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Movie movie = null;
        mDetailsPresenter = new MovieDetailsPresenter(this, new MoviesRepositoryImpl(context, new MoviesServiceImpl()));
        if (getArguments() != null && getArguments().getParcelable(MOVIE_EXTRA) != null) {
            movie = getArguments().getParcelable(MOVIE_EXTRA);
        }
        if (movie != null) {
            mDetailsPresenter.getMoviesDetails(movie.getId(), new MoviesRepository.MoviesRepositoryCallback<MovieDetails>() {
                @Override
                public void onSuccess(MovieDetails movies) {
                    populateViews(movies);
                }

                @Override
                public void onFailure(String message) {
                    Log.e(TAG, message);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_detail, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        return view;
    }

    private void populateViews(MovieDetails movies) {
        if (movies != null) {
            mMovieDetails = movies;
            Picasso.with(getActivity())
                    .load(MoviesServiceImpl.getPosterPath(MoviesServiceApi.W154, mMovieDetails.getPoster_path()))
                    .into(mMoviePosterImgVw);
            mMovieTitleTxtVw.setText(mMovieDetails.getOriginalTitle());
            try {
                mReleaseDateTxtVw.setText(mMovieDetails.getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mMovieOverviewTxtVw.setText(mMovieDetails.getOverview());
            mMovieRatingBar.setRating(mMovieDetails.getVoteAverage());
            for (Genres genres : mMovieDetails.getGenres()) {
                TextView genreItemView = (TextView) LayoutInflater.from(mGenreContainer.getContext()).inflate(R.layout.item_genre, mGenreContainer, false);
                genreItemView.setText(genres.getName());
                mGenreContainer.addView(genreItemView);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }
}
