package com.fabinpaul.project_2_popularmovies.features.moviesdetail.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;

import java.util.ArrayList;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 11:45 AM.
 */

public class MovieDetails extends Movie implements Parcelable {

    private Double budget;
    private ArrayList<Genres> genres;
    private String status;
    private VideoList videos;
    private Integer runtime;
    private ArrayList<SpokenLanguages> spoken_languages;
    private String homepage;
    private ArrayList<ProductionCountries> production_countries;
    private ArrayList<ProductionCompanies> production_companies;
    private String imdb_id;
    private String tagline;
    private Double revenue;

    public Double getBudget() {
        return budget;
    }

    public ArrayList<Genres> getGenres() {
        return genres;
    }

    public String getStatus() {
        return status;
    }

    public VideoList getVideos() {
        return videos;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public ArrayList<SpokenLanguages> getSpoken_languages() {
        return spoken_languages;
    }

    public String getHomepage() {
        return homepage;
    }

    public ArrayList<ProductionCountries> getProduction_countries() {
        return production_countries;
    }

    public ArrayList<ProductionCompanies> getProduction_companies() {
        return production_companies;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public String getTagline() {
        return tagline;
    }

    public Double getRevenue() {
        return revenue;
    }

    protected MovieDetails(Parcel in) {
        budget = in.readByte() == 0x00 ? null : in.readDouble();
        if (in.readByte() == 0x01) {
            genres = new ArrayList<Genres>();
            in.readList(genres, Genres.class.getClassLoader());
        } else {
            genres = null;
        }
        status = in.readString();
        videos = (VideoList) in.readValue(VideoList.class.getClassLoader());
        runtime = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            spoken_languages = new ArrayList<SpokenLanguages>();
            in.readList(spoken_languages, SpokenLanguages.class.getClassLoader());
        } else {
            spoken_languages = null;
        }
        homepage = in.readString();
        if (in.readByte() == 0x01) {
            production_countries = new ArrayList<ProductionCountries>();
            in.readList(production_countries, ProductionCountries.class.getClassLoader());
        } else {
            production_countries = null;
        }
        if (in.readByte() == 0x01) {
            production_companies = new ArrayList<ProductionCompanies>();
            in.readList(production_companies, ProductionCompanies.class.getClassLoader());
        } else {
            production_companies = null;
        }
        imdb_id = in.readString();
        tagline = in.readString();
        revenue = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (budget == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(budget);
        }
        if (genres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genres);
        }
        dest.writeString(status);
        dest.writeValue(videos);
        if (runtime == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(runtime);
        }
        if (spoken_languages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(spoken_languages);
        }
        dest.writeString(homepage);
        if (production_countries == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(production_countries);
        }
        if (production_companies == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(production_companies);
        }
        dest.writeString(imdb_id);
        dest.writeString(tagline);
        if (revenue == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(revenue);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieDetails> CREATOR = new Parcelable.Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };
}