package com.fabinpaul.project_2_popularmovies.features.moviesdetail.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 12/6/2016 9:40 AM.
 */

public class ReviewList implements Parcelable {

    private int page;
    private ArrayList<Review> results;
    private int total_pages;
    private int total_results;

    public int getPage() {
        return page;
    }

    public ArrayList<Review> getResults() {
        return results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    @SuppressWarnings("WeakerAccess")
    protected ReviewList(Parcel in) {
        page = in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<>();
            in.readList(results, Review.class.getClassLoader());
        } else {
            results = null;
        }
        total_pages = in.readInt();
        total_results = in.readInt();
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
        dest.writeInt(total_pages);
        dest.writeInt(total_results);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReviewList> CREATOR = new Parcelable.Creator<ReviewList>() {
        @Override
        public ReviewList createFromParcel(Parcel in) {
            return new ReviewList(in);
        }

        @Override
        public ReviewList[] newArray(int size) {
            return new ReviewList[size];
        }
    };
}