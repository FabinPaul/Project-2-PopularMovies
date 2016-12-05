package com.fabinpaul.project_2_popularmovies.features.moviesdetail.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 11:53 AM.
 */
public class ProductionCompanies implements Parcelable {

    private String id;
    private String name;

    protected ProductionCompanies(Parcel in) {
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
    public static final Parcelable.Creator<ProductionCompanies> CREATOR = new Parcelable.Creator<ProductionCompanies>() {
        @Override
        public ProductionCompanies createFromParcel(Parcel in) {
            return new ProductionCompanies(in);
        }

        @Override
        public ProductionCompanies[] newArray(int size) {
            return new ProductionCompanies[size];
        }
    };
}
