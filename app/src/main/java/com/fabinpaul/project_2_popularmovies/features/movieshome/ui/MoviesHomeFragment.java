package com.fabinpaul.project_1_popularmovies.features.movieshome.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fabinpaul.project_1_popularmovies.R;
import com.fabinpaul.project_1_popularmovies.features.moviesdetail.ui.MoviesDetailActivity;
import com.fabinpaul.project_1_popularmovies.features.movieshome.ItemOffsetDecoration;
import com.fabinpaul.project_1_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_1_popularmovies.features.movieshome.data.MovieList;
import com.fabinpaul.project_1_popularmovies.features.movieshome.logic.MoviesListContract;
import com.fabinpaul.project_1_popularmovies.features.movieshome.logic.MoviesListPresenter;
import com.fabinpaul.project_1_popularmovies.framework.network.MoviesServiceImpl;
import com.fabinpaul.project_1_popularmovies.framework.repository.MoviesRepository;
import com.fabinpaul.project_1_popularmovies.framework.repository.MoviesRepositoryImpl;
import com.fabinpaul.project_1_popularmovies.framework.ui.EndlessRecyclerViewScrollListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesHomeFragment extends Fragment implements MoviesListContract.View {

    private static final int GRID_SPAN_COUNT = 2;
    private static final String TAG = MoviesHomeFragment.class.getSimpleName();

    @BindView(R.id.movies_recycvw_list)
    RecyclerView mMoviesRecycVw;

    private MoviesListPresenter mMoviesListPresenter;
    private MoviesListAdapter mMoviesListAdapter;
    private SharedPreferences mSortMoviePreferences;
    private MoviesRepository mMoviesRepository;
    private Unbinder mUnBinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMoviesRepository = new MoviesRepositoryImpl(context, new MoviesServiceImpl());
        mMoviesListPresenter = new MoviesListPresenter(this, mMoviesRepository);
        setHasOptionsMenu(true);
        mSortMoviePreferences = context.getSharedPreferences(getString(R.string.movie_pref), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_home, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
        mMoviesRecycVw.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.movie_grid_item_offset);
        mMoviesRecycVw.addItemDecoration(itemDecoration);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mMoviesListPresenter.loadMoreDataFromApi(page + 1);
            }
        };
        mMoviesRecycVw.addOnScrollListener(scrollListener);
        mMoviesListAdapter = MoviesListAdapter.setMoviesListAdapter(mMoviesListPresenter, mMoviesRecycVw);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_home, menu);
        int sort_pref = getMovieSortPreference();
        MenuItem item = menu.findItem(R.id.movie_sort).getSubMenu().findItem(sort_pref);
        if (item != null)
            switch (item.getItemId()) {
                case R.id.movies_popular_sort:
                    item.setChecked(true);
                    mMoviesListPresenter.changeMovieSort(MoviesListContract.POPULAR_MOVIE);
                    break;
                case R.id.movies_top_rated_sort:
                    item.setChecked(true);
                    mMoviesListPresenter.changeMovieSort(MoviesListContract.TOP_RATED_MOVIE);
                    break;
                default:
                    setUpDefaultOptionsMenu(menu);
                    break;
            }
        else
            setUpDefaultOptionsMenu(menu);
    }

    private void setUpDefaultOptionsMenu(Menu menu) {
        menu.findItem(R.id.movie_sort)
                .getSubMenu()
                .findItem(R.id.movies_popular_sort)
                .setChecked(true);
        getPopularMovies();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.movies_popular_sort:
                item.setChecked(true);
                mMoviesListPresenter.changeMovieSort(MoviesListContract.POPULAR_MOVIE);
                saveMovieSortPreference(R.id.movies_popular_sort);
                return true;
            case R.id.movies_top_rated_sort:
                item.setChecked(true);
                mMoviesListPresenter.changeMovieSort(MoviesListContract.TOP_RATED_MOVIE);
                saveMovieSortPreference(R.id.movies_top_rated_sort);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMoviesRepository.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
        mMoviesListAdapter.onDestroyView();
    }

    @Override
    public void showMovieDetails(Movie pMovie) {
        if (getActivity() != null && pMovie != null)
            MoviesDetailActivity.startActivity(getActivity(), pMovie);
        else
            Log.d(TAG, "pMovie or getActivity is null, Check the parameters");
    }

    private void setToolbarTitle(String pTitle) {
        if (getActivity() != null) {
            getActivity().setTitle(pTitle);
        }
    }

    private void saveMovieSortPreference(int pMenuId) {
        SharedPreferences.Editor editor = mSortMoviePreferences.edit();
        editor.putInt(getString(R.string.sort_movie_pref), pMenuId);
        editor.apply();
    }

    private int getMovieSortPreference() {
        return mSortMoviePreferences.getInt(getString(R.string.sort_movie_pref), R.id.movies_popular_sort);
    }

    public void getPopularMovies() {
        setToolbarTitle(getString(R.string.popular_movies));
        mMoviesListPresenter.getPopularMovies(1, new MoviesRepository.MoviesRepositoryCallback<MovieList>() {
            @Override
            public void onSuccess(MovieList movies) {
                mMoviesListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(String message) {
            }
        });
    }

    public void getTopRatedMovies() {
        setToolbarTitle(getString(R.string.top_rated_movies));
        mMoviesListPresenter.getTopRatedMovies(1, new MoviesRepository.MoviesRepositoryCallback<MovieList>() {
            @Override
            public void onSuccess(MovieList movies) {
                mMoviesListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }
}
