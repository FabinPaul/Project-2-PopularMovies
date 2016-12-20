package com.fabinpaul.project_2_popularmovies.features.moviesdetail.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 11:49 AM.
 */
public class Video implements Parcelable {

    public static final String YOUTUBE = "YouTube";

    private String site;
    private String id;
    private String iso_639_1;
    private String name;
    private String type;
    private String key;
    private String iso_3166_1;
    private String size;

    public String getSite() {
        return site;
    }

    public String getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public String getSize() {
        return size;
    }

    public boolean isYoutubeVideo() {
        return site.equalsIgnoreCase(YOUTUBE);
    }

    @SuppressWarnings("WeakerAccess")
    protected Video(Parcel in) {
        site = in.readString();
        id = in.readString();
        iso_639_1 = in.readString();
        name = in.readString();
        type = in.readString();
        key = in.readString();
        iso_3166_1 = in.readString();
        size = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(site);
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(key);
        dest.writeString(iso_3166_1);
        dest.writeString(size);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
