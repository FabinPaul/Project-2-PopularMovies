package com.fabinpaul.project_2_popularmovies.features.movieshome.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fabinpaul.project_2_popularmovies.R;
import com.fabinpaul.project_2_popularmovies.features.movieshome.ItemOffsetDecoration;
import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;
import com.fabinpaul.project_2_popularmovies.features.movieshome.logic.MoviesListContract;
import com.fabinpaul.project_2_popularmovies.features.movieshome.logic.MoviesListPresenter;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceImpl;
import com.fabinpaul.project_2_popularmovies.framework.repository.MoviesRepository;
import com.fabinpaul.project_2_popularmovies.framework.repository.MoviesRepositoryImpl;
import com.fabinpaul.project_2_popularmovies.framework.ui.EndlessRecyclerViewScrollListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressWarnings("WeakerAccess")
public class MoviesHomeFragment extends Fragment implements MoviesListContract.View {

    private static final int GRID_SPAN_COUNT = 2;
    private static final String TAG = MoviesHomeFragment.class.getSimpleName();
    private static final String CURRENT_POSITION = "com.fabinpaul.project_2_popularmovies.CurrentMoviePosition";
    private static final String CURRENT_MOVIE = "com.fabinpaul.project_2_popularmovies.CurrentDetailMovie";
    private static final String CURRENT_TITLE = "com.fabinpaul.project_2_popularmovies.CurrentMoviTitle";

    @BindView(R.id.movies_recycvw_list)
    RecyclerView mMoviesRecycVw;
    @BindView(R.id.movies_swiperefresh)
    SwipeRefreshLayout mMoviesSwipeRefresh;

    private MoviesListPresenter mMoviesListPresenter;
    private MoviesListAdapter mMoviesListAdapter;
    private SharedPreferences mSortMoviePreferences;
    private MoviesRepository mMoviesRepository;
    private Unbinder mUnBinder;
    private boolean mIsTwoPaneLayout;
    private boolean isMoviesLoaded;
    private GridLayoutManager mGridLayoutManager;
    private int mCurrentMoviePos;
    private String mToolbarTitle;

    public void setAsTwoPaneLayout(boolean isTwoPaneLayout) {
        mIsTwoPaneLayout = isTwoPaneLayout;
    }

    interface Callback {
        void onItemSelected(@NonNull Movie pMovie);
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
        outState.putInt(CURRENT_POSITION, mGridLayoutManager.findFirstVisibleItemPosition());
        outState.putInt(CURRENT_MOVIE, mCurrentMoviePos);
        if (mToolbarTitle != null)
            outState.putString(CURRENT_TITLE, mToolbarTitle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMoviesRepository.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_POSITION)) {
            int currentPosition = savedInstanceState.getInt(CURRENT_POSITION);
            mGridLayoutManager.scrollToPosition(currentPosition);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_MOVIE)) {
            mCurrentMoviePos = savedInstanceState.getInt(CURRENT_MOVIE);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_TITLE)) {
            setToolbarTitle(savedInstanceState.getString(CURRENT_TITLE));
        }
    }

    private void attachFragment(Context context) {
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
        mGridLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
        mMoviesRecycVw.setLayoutManager(mGridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.movie_grid_item_offset);
        mMoviesRecycVw.addItemDecoration(itemDecoration);
        mMoviesSwipeRefresh.setColorSchemeResources(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mGridLayoutManager) {
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMoviesSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                if (mMoviesRecycVw != null)
                    mMoviesRecycVw.setLayoutFrozen(true);
                mMoviesListPresenter.refreshPage();
            }
        });
        if (mMoviesListPresenter != null)
            mMoviesListPresenter.loadFavouriteMovies();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_home, menu);
        int sort_pref = getMovieSortPreference();
        MenuItem item = menu.findItem(R.id.movie_sort).getSubMenu().findItem(sort_pref);
        int sortId;
        if (item != null)
            switch (item.getItemId()) {
                case R.id.movies_popular_sort:
                    item.setChecked(true);
                    sortId = MoviesListContract.POPULAR_MOVIE;
                    break;
                case R.id.movies_top_rated_sort:
                    item.setChecked(true);
                    sortId = MoviesListContract.TOP_RATED_MOVIE;
                    break;
                case R.id.movies_favourite_sort:
                    item.setChecked(true);
                    sortId = MoviesListContract.FAVOURITE_MOVIE;
                    break;
                default:
                    sortId = setUpDefaultOptionsMenu(menu);
                    break;
            }
        else {
            sortId = setUpDefaultOptionsMenu(menu);
        }
        if (!isMoviesLoaded) {
            mMoviesListPresenter.changeMovieSort(sortId);
            isMoviesLoaded = true;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private
    @MoviesListContract.MovieSortStatus
    int setUpDefaultOptionsMenu(Menu menu) {
        menu.findItem(R.id.movie_sort)
                .getSubMenu()
                .findItem(R.id.movies_popular_sort)
                .setChecked(true);
        return MoviesListContract.POPULAR_MOVIE;
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
            case R.id.movies_favourite_sort:
                item.setChecked(true);
                mMoviesListPresenter.changeMovieSort(MoviesListContract.FAVOURITE_MOVIE);
                saveMovieSortPreference(R.id.movies_favourite_sort);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMoviesRepository.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
        mMoviesListAdapter.onDestroyView();
    }

    @Override
    public void showMovieDetails(Movie pMovie, int pPosition) {
        if (getActivity() != null && pMovie != null) {
            mCurrentMoviePos = pPosition;
            pMovie.setFavourite(mMoviesListPresenter.isFavouriteMovie(pMovie.getId()));
            ((Callback) getActivity()).onItemSelected(pMovie);
        } else {
            mMoviesListAdapter.notifyDataSetChanged();
            Log.d(TAG, "pMovie or getActivity is null, Check the parameters");
        }
    }

    @Override
    public void getFavouriteMovies() {
        prepareViewForRequest(getString(R.string.fav_movies));
        mMoviesListPresenter.getFavouriteMovies(new MoviesRepository.MoviesLoaderCallback() {
            @Override
            public void onSuccess() {
                mMoviesListAdapter.notifyDataSetChanged();
                onRefreshComplete();
            }

            @Override
            public void onReset() {
                mMoviesListAdapter.notifyDataSetChanged();
                onRefreshComplete();
            }
        });
    }

    private void setToolbarTitle(String pTitle) {
        if (getActivity() != null) {
            mToolbarTitle = pTitle;
            getActivity().setTitle(pTitle);
        }
    }

    private void saveMovieSortPreference(int pMenuId) {
        SharedPreferences.Editor editor = mSortMoviePreferences.edit();
        editor.putInt(getString(R.string.sort_movie_pref), pMenuId);
        editor.apply();
    }

    private void onRefreshComplete() {
        if (mMoviesSwipeRefresh != null && mMoviesSwipeRefresh.isRefreshing()) {
            mMoviesSwipeRefresh.setRefreshing(false);
        }
        if (mMoviesRecycVw != null && mMoviesRecycVw.isLayoutFrozen())
            mMoviesRecycVw.setLayoutFrozen(false);
    }

    private void loadInitialDetailFragment() {
        if (mIsTwoPaneLayout && getActivity() != null) {
            ((Callback) getActivity()).onItemSelected(mMoviesListPresenter.getMovie(mCurrentMoviePos));
        }
    }

    private int getMovieSortPreference() {
        return mSortMoviePreferences.getInt(getString(R.string.sort_movie_pref), R.id.movies_popular_sort);
    }

    public void getPopularMovies() {
        prepareViewForRequest(getString(R.string.popular_movies));
        mMoviesListPresenter.getPopularMovies(1, new MoviesRepository.MoviesRepositoryCallback() {
            @Override
            public void onSuccess() {
                mMoviesListAdapter.notifyDataSetChanged();
                onRefreshComplete();
                loadInitialDetailFragment();
            }

            @Override
            public void onFailure(String message) {
                onRefreshComplete();
            }
        });
    }

    public void getTopRatedMovies() {
        prepareViewForRequest(getString(R.string.top_rated_movies));
        mMoviesListPresenter.getTopRatedMovies(1, new MoviesRepository.MoviesRepositoryCallback() {
            @Override
            public void onSuccess() {
                mMoviesListAdapter.notifyDataSetChanged();
                onRefreshComplete();
                loadInitialDetailFragment();
            }

            @Override
            public void onFailure(String message) {
                onRefreshComplete();
            }
        });
    }

    public void prepareViewForRequest(String title) {
        mCurrentMoviePos = 0;
        if (mMoviesRecycVw != null)
            mMoviesRecycVw.setLayoutFrozen(true);
        setToolbarTitle(title);
    }
}
