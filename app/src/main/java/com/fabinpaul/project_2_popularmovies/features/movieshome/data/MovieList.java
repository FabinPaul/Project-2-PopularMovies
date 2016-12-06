package com.fabinpaul.project_2_popularmovies.features.movieshome.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabin Paul on 11/5/2016.
 */

public class MovieList implements Parcelable {

    private int page;
    private List<Movie> results = new ArrayList<>();
    private int total_results;
    private int total_pages;

    public int getPage() {
        return page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public int getTotalResults() {
        return total_results;
    }

    public int getTotalPages() {
        return total_pages;
    }

    public void updateMovieList(MovieList pMovieList) {
        if (pMovieList.getPage() > page) {
            results.addAll(pMovieList.getResults());
            total_pages = pMovieList.getTotalPages();
            total_results = pMovieList.getTotalResults();
            page = pMovieList.getPage();
        }
    }

    public void clear() {
        results.clear();
        page = 0;
        total_results = 0;
        total_pages = 0;
    }

    public MovieList() {
    }

    protected MovieList(Parcel in) {
        page = in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<Movie>();
            in.readList(results, Movie.class.getClassLoader());
        } else {
            results = null;
        }
        total_results = in.readInt();
        total_pages = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
        dest.writeInt(total_results);
        dest.writeInt(total_pages);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieList> CREATOR = new Parcelable.Creator<MovieList>() {
        @Override
        public MovieList createFromParcel(Parcel in) {
            return new MovieList(in);
        }

        @Override
        public MovieList[] newArray(int size) {
            return new MovieList[size];
        }
    };
}
