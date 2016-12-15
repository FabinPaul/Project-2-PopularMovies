package com.fabinpaul.project_2_popularmovies.features.movieshome.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fabin Paul on 11/5/20161:16 PM 1:16 PM.
 */

public class Movie implements Parcelable {


    protected Boolean adult;
    protected List<Integer> genre_ids;
    protected Integer id;
    protected String title;
    protected String original_title;
    protected String overview;
    protected String release_date;
    protected String poster_path;
    protected String backdrop_path;
    protected Float popularity;
    protected String original_language;
    protected Integer vote_count;
    protected Boolean video;
    protected Float vote_average;
    protected Boolean isFavourite;

    public Movie() {
        genre_ids = new ArrayList<Integer>();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public void setVote_average(Float vote_average) {
        this.vote_average = vote_average;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDateString(){
        return release_date;
    }

    public String getReleaseDate() throws ParseException {
        SimpleDateFormat  sourceFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat destinationFormat = new SimpleDateFormat("dd,MMM yyyy", Locale.US);
        Date date = sourceFormat.parse(release_date);
        return destinationFormat.format(date);
    }

    public List<Integer> getGenreIds() {
        return genre_ids;
    }

    public Integer getId() {
        return id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public Float getPopularity() {
        return popularity;
    }

    public Integer getVoteCount() {
        return vote_count;
    }

    public Boolean getVideo() {
        return video;
    }

    public Float getVoteAverage() {
        return vote_average;
    }

    protected Movie(Parcel in) {
        poster_path = in.readString();
        byte adultVal = in.readByte();
        adult = adultVal == 0x02 ? null : adultVal != 0x00;
        overview = in.readString();
        release_date = in.readString();
        if (in.readByte() == 0x01) {
            genre_ids = new ArrayList<Integer>();
            in.readList(genre_ids, Integer.class.getClassLoader());
        } else {
            genre_ids = null;
        }
        id = in.readByte() == 0x00 ? null : in.readInt();
        original_title = in.readString();
        original_language = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        popularity = in.readByte() == 0x00 ? null : in.readFloat();
        vote_count = in.readByte() == 0x00 ? null : in.readInt();
        byte videoVal = in.readByte();
        video = videoVal == 0x02 ? null : videoVal != 0x00;
        vote_average = in.readByte() == 0x00 ? null : in.readFloat();
        byte favourite = in.readByte();
        isFavourite = favourite == 0x02 ? null : favourite != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        if (adult == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (adult ? 0x01 : 0x00));
        }
        dest.writeString(overview);
        dest.writeString(release_date);
        if (genre_ids == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genre_ids);
        }
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(original_title);
        dest.writeString(original_language);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        if (popularity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(popularity);
        }
        if (vote_count == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(vote_count);
        }
        if (video == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (video ? 0x01 : 0x00));
        }
        if (vote_average == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(vote_average);
        }

        if (isFavourite == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isFavourite ? 0x01 : 0x00));
        }
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
