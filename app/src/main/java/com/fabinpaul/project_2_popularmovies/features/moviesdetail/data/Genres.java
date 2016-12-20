package com.fabinpaul.project_2_popularmovies.features.moviesdetail.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 11:53 AM.
 */
public class Genres implements Parcelable {

    private String id;
    private String name;

    public String getName() {
        return name;
    }

    @SuppressWarnings("WeakerAccess")
    protected Genres(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Genres> CREATOR = new Parcelable.Creator<Genres>() {
        @Override
        public Genres createFromParcel(Parcel in) {
            return new Genres(in);
        }

        @Override
        public Genres[] newArray(int size) {
            return new Genres[size];
        }
    };
}
