package com.fabinpaul.project_2_popularmovies.features.moviesdetail.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 11:50 AM.
 */

public class SpokenLanguages implements Parcelable {

    private String iso_639_1;
    private String name;

    @SuppressWarnings("WeakerAccess")
    protected SpokenLanguages(Parcel in) {
        iso_639_1 = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iso_639_1);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SpokenLanguages> CREATOR = new Parcelable.Creator<SpokenLanguages>() {
        @Override
        public SpokenLanguages createFromParcel(Parcel in) {
            return new SpokenLanguages(in);
        }

        @Override
        public SpokenLanguages[] newArray(int size) {
            return new SpokenLanguages[size];
        }
    };
}
