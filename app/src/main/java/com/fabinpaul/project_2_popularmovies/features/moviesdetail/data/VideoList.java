package com.fabinpaul.project_2_popularmovies.features.moviesdetail.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 11:48 AM.
 */

public class VideoList implements Parcelable {

    private ArrayList<Video> results;


    public ArrayList<Video> getResults() {
        return results;
    }

    @SuppressWarnings("WeakerAccess")
    protected VideoList(Parcel in) {
        if (in.readByte() == 0x01) {
            results = new ArrayList<>();
            in.readList(results, Video.class.getClassLoader());
        } else {
            results = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VideoList> CREATOR = new Parcelable.Creator<VideoList>() {
        @Override
        public VideoList createFromParcel(Parcel in) {
            return new VideoList(in);
        }

        @Override
        public VideoList[] newArray(int size) {
            return new VideoList[size];
        }
    };
}
