package com.fabinpaul.project_1_popularmovies.features.movieshome.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fabinpaul.project_1_popularmovies.R;
import com.fabinpaul.project_1_popularmovies.features.movieshome.logic.MoviesListPresenter;
import com.fabinpaul.project_1_popularmovies.framework.network.MoviesServiceApi;
import com.fabinpaul.project_1_popularmovies.framework.network.MoviesServiceImpl;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Fabin Paul on 11/5/2016 6:55 PM.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

    private final MoviesListPresenter mMoviesListPresenter;
    private Context mContext;
    private Unbinder mUnBinder;

    static MoviesListAdapter setMoviesListAdapter(MoviesListPresenter pMoviesListPresenter, RecyclerView pRecyclerView) {
        MoviesListAdapter moviesListAdapter = new MoviesListAdapter(pRecyclerView.getContext(), pMoviesListPresenter);
        pRecyclerView.setAdapter(moviesListAdapter);
        return moviesListAdapter;
    }

    private MoviesListAdapter(Context pContext, MoviesListPresenter pMoviesListPresenter) {
        mContext = pContext;
        mMoviesListPresenter = pMoviesListPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext)
                //.load(MoviesServiceApi.IMAGE_BASE_URL + mMoviesListPresenter.getMovie(position).getPoster_path())
                .load(MoviesServiceImpl.getPosterPath(MoviesServiceApi.W300, mMoviesListPresenter
                                                                            .getMovie(position)
                                                                            .getPoster_path()))
                .into(holder.mMoviePosterImgVw);
    }

    public void onDestroyView() {
        if (mUnBinder != null)
            mUnBinder.unbind();
    }

    @Override
    public int getItemCount() {
        return mMoviesListPresenter.getTotalMoviesCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_item_poster_image_vw)
        ImageView mMoviePosterImgVw;

        ViewHolder(View itemView) {
            super(itemView);
            mUnBinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View pView) {
            mMoviesListPresenter.moviesOnClick(getAdapterPosition());
        }
    }
}
