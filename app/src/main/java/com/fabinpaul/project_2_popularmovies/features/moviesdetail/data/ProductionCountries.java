package com.fabinpaul.project_2_popularmovies.features.moviesdetail.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 11:52 AM.
 */
public class ProductionCountries implements Parcelable {

    private String name;
    private String iso_3166_1;

    @SuppressWarnings("WeakerAccess")
    protected ProductionCountries(Parcel in) {
        name = in.readString();
        iso_3166_1 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(iso_3166_1);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductionCountries> CREATOR = new Parcelable.Creator<ProductionCountries>() {
        @Override
        public ProductionCountries createFromParcel(Parcel in) {
            return new ProductionCountries(in);
        }

        @Override
        public ProductionCountries[] newArray(int size) {
            return new ProductionCountries[size];
        }
    };
}
