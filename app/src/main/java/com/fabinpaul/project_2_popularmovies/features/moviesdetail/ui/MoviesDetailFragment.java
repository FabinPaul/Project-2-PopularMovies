package com.fabinpaul.project_2_popularmovies.features.moviesdetail.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fabinpaul.project_2_popularmovies.R;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.Genres;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.Review;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.Video;
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
import butterknife.OnClick;
import butterknife.Unbinder;

@SuppressWarnings("WeakerAccess")
public class MoviesDetailFragment extends Fragment implements MovieDetailsContract.View {

    private static final String TAG = MoviesDetailFragment.class.getSimpleName();

    private Unbinder mUnBinder;
    private ShareActionProvider mShareActionProvider;

    private String mMovieTitle;

    @BindView(R.id.movie_details_overview_txtvw)
    TextView mMovieOverviewTxtVw;
    @BindView(R.id.movie_detail_rating_bar)
    RatingBar mMovieRatingBar;
    @BindView(R.id.movie_detail_rating)
    TextView mMovieRatingTxtVw;
    @BindView(R.id.movies_detail_poster_imgvw)
    ProportionalImageView mMoviePosterImgVw;
    @BindView(R.id.movie_details_title_txtvw)
    TextView mMovieTitleTxtVw;
    @BindView(R.id.movie_details_release_date_txtvw)
    TextView mReleaseDateTxtVw;
    @BindView(R.id.movie_details_genre_list)
    FlexboxLayout mGenreContainer;
    @BindView(R.id.movie_details_video_list)
    RecyclerView mVideoListRecyclerVw;
    @BindView(R.id.movie_details_runtime_txtvw)
    TextView mRuntimeTxtVw;
    @BindView(R.id.movie_details_review_container)
    ViewGroup mReviewContainer;
    @BindView(R.id.movies_detail_background_imgvw)
    ProportionalImageView mBackdropImgVw;
    @BindView(R.id.movie_fav_fab)
    FloatingActionButton mFavFAB;

    private MovieVideosListAdapter mVideosListAdapter;
    private MovieDetailsPresenter mDetailsPresenter;
    private MoviesRepository mMoviesRepository;
    private boolean isFavourite;

    public MoviesDetailFragment() {
    }

    public static MoviesDetailFragment newInstance(Movie pMovie) {
        MoviesDetailFragment fragment = new MoviesDetailFragment();
        Bundle movieExtra = new Bundle();
        movieExtra.putParcelable(MoviesRepository.MOVIE_EXTRA, pMovie);
        fragment.setArguments(movieExtra);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        attachFragment(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            attachFragment(activity);
        }
    }

    @OnClick(R.id.movie_fav_fab)
    public void onFavouriteMovieToggle(View view) {
        if (mDetailsPresenter != null && mDetailsPresenter.getMovieDetails() != null && !mDetailsPresenter.getMovieDetails().isFavourite()) {
            mDetailsPresenter.setMovieAsFavourite(true);
            mFavFAB.setImageResource(R.drawable.ic_favorite_24dp);
        } else if (mDetailsPresenter != null && mDetailsPresenter.getMovieDetails() != null && mDetailsPresenter.getMovieDetails().isFavourite()) {
            mDetailsPresenter.setMovieAsFavourite(false);
            mFavFAB.setImageResource(R.drawable.ic_favorite_border_24dp);
        }
    }

    private void attachFragment(Context context) {
        Movie movie = null;
        mMoviesRepository = new MoviesRepositoryImpl(context, new MoviesServiceImpl());
        mDetailsPresenter = new MovieDetailsPresenter(this, mMoviesRepository);
        if (getArguments() != null && getArguments().getParcelable(MoviesRepository.MOVIE_EXTRA) != null) {
            movie = getArguments().getParcelable(MoviesRepository.MOVIE_EXTRA);
        }
        if (movie != null) {
            mMovieTitle = movie.getTitle();
            isFavourite = movie.isFavourite();
            mDetailsPresenter.getMoviesDetails(movie.getId(), new MoviesRepository.MoviesRepositoryCallback() {
                @Override
                public void onSuccess() {
                    populateViews();
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

        if (view.findViewById(R.id.toolbar) != null) {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            toolbar.setTitle("");
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMoviesRepository.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMoviesRepository.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mMoviesRepository.onSaveStateInstance(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMoviesRepository.onRestoreInstanceState(savedInstanceState);
    }


    private void populateViews() {
        if (mDetailsPresenter.getMovieDetails() != null) {
            if (isFavourite)
                mFavFAB.setImageResource(R.drawable.ic_favorite_24dp);
            Picasso.with(getActivity())
                    .load(MoviesServiceImpl.getPosterPath(MoviesServiceApi.W154, mDetailsPresenter.getMovieDetails().getPoster_path()))
                    .into(mMoviePosterImgVw);
            Picasso.with(getActivity())
                    .load(MoviesServiceImpl.getBackDropPath(MoviesServiceApi.W500, mDetailsPresenter.getMovieDetails().getBackdropPath()))
                    .into(mBackdropImgVw);
            mMovieTitleTxtVw.setText(mDetailsPresenter.getMovieDetails().getOriginalTitle());
            try {
                mReleaseDateTxtVw.setText(mDetailsPresenter.getMovieDetails().getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mMovieOverviewTxtVw.setText(mDetailsPresenter.getMovieDetails().getOverview());
            mMovieRatingTxtVw.setText(String.valueOf(mDetailsPresenter.getMovieDetails().getVoteAverage()));
            mMovieRatingBar.setRating(mDetailsPresenter.getMovieDetails().getVoteAverage());
            for (Genres genres : mDetailsPresenter.getMovieDetails().getGenres()) {
                TextView genreItemView = (TextView) LayoutInflater.from(mGenreContainer.getContext()).inflate(R.layout.item_genre, mGenreContainer, false);
                genreItemView.setText(genres.getName());
                mGenreContainer.addView(genreItemView);
            }
            mVideosListAdapter = MovieVideosListAdapter.setMoviesVideoListAdapter(mVideoListRecyclerVw, mDetailsPresenter);
            mRuntimeTxtVw.setText(getString(R.string.runtime_units, mDetailsPresenter.getMovieDetails().getRuntime()));
            for (Review review : mDetailsPresenter.getMovieDetails().getReviews().getResults()) {
                View view = LayoutInflater.from(mReviewContainer.getContext()).inflate(R.layout.item_review, mReviewContainer, false);
                TextView reviewerNameTxtVw = (TextView) view.findViewById(R.id.review_txtvw_user_name);
                TextView reviewTxtVw = (TextView) view.findViewById(R.id.review_txtvw);
                reviewerNameTxtVw.setText(review.getAuthor());
                reviewTxtVw.setText(review.getContent());
                mReviewContainer.addView(view);
            }
            updateShareActionProvider(mDetailsPresenter.getVideoAtPosition(0));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
        if (mVideosListAdapter != null)
            mVideosListAdapter.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMoviesRepository.onDestroy();
    }

    @Override
    public void openYouTubeVideo(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void updateShareActionProvider(Video video) {
        if (mShareActionProvider == null)
            return;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mMovieTitle);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, MoviesServiceApi.YOUTUBE_URL + video.getKey());
        mShareActionProvider.setShareIntent(sharingIntent);
    }
}
