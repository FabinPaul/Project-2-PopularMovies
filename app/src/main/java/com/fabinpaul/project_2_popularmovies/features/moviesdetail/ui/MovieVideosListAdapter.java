package com.fabinpaul.project_2_popularmovies.features.moviesdetail.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fabinpaul.project_2_popularmovies.R;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.data.Video;
import com.fabinpaul.project_2_popularmovies.features.moviesdetail.logic.MovieDetailsPresenter;
import com.fabinpaul.project_2_popularmovies.framework.network.MoviesServiceApi;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 12/6/2016 10:31 AM.
 */

public class MovieVideosListAdapter extends RecyclerView.Adapter<MovieVideosListAdapter.ViewHolder> {

    private Unbinder mUnBinder;
    private MovieDetailsPresenter mDetailsPresenter;
    private Context mContext;

    static MovieVideosListAdapter setMoviesVideoListAdapter(RecyclerView pMoviesVideoListRecycrVw, MovieDetailsPresenter pMoviesDetailsPresenter) {
        MovieVideosListAdapter movieVideosListAdapter = new MovieVideosListAdapter(pMoviesVideoListRecycrVw.getContext(), pMoviesDetailsPresenter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(pMoviesVideoListRecycrVw.getContext(), 2);
        pMoviesVideoListRecycrVw.setLayoutManager(layoutManager);
        pMoviesVideoListRecycrVw.setAdapter(movieVideosListAdapter);
        return movieVideosListAdapter;
    }

    private MovieVideosListAdapter(Context context, MovieDetailsPresenter detailsPresenter) {
        mDetailsPresenter = detailsPresenter;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mDetailsPresenter.getVideoList() == null)
            return;
        Video video = mDetailsPresenter.getVideoAtPosition(position);
        if (video.isYoutubeVideo()) {
            Picasso.with(mContext)
                    .load(String.format(MoviesServiceApi.YOUTUBE_THUMBNAIL_URL, video.getKey()))
                    .placeholder(new ColorDrawable(mContext.getResources().getColor(android.R.color.black)))
                    .into(holder.mVideoThumbnailImgVw);
        }
    }

    @Override
    public int getItemCount() {
        return mDetailsPresenter.getVideoCount();
    }


    public void onDestroyView() {
        if (mUnBinder != null)
            mUnBinder.unbind();
        if (mContext != null)
            mContext = null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_video_thumbnail)
        ImageView mVideoThumbnailImgVw;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mUnBinder = ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            mDetailsPresenter.onMovieVideoClick(mDetailsPresenter.getVideoAtPosition(getAdapterPosition()));
        }
    }
}
