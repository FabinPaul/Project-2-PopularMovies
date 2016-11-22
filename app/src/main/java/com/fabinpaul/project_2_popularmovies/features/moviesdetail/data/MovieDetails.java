package com.fabinpaul.project_2_popularmovies.features.moviesdetail.data;

import com.fabinpaul.project_2_popularmovies.features.movieshome.data.Movie;

import java.util.ArrayList;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/22/2016 11:45 AM.
 */

public class MovieDetails extends Movie {

    private Double budget;
    private ArrayList<Genres> genres;
    private String status;
    private VideoList videos;
    private Integer runtime;
    private ArrayList<SpokenLanguages> spoken_languages;
    private String homepage;
    private ArrayList<ProductionCountries> production_countries;
    private ArrayList<ProductionCompanies> production_companies;
    private Integer imdb_id;
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

    public Integer getImdb_id() {
        return imdb_id;
    }

    public String getTagline() {
        return tagline;
    }

    public Double getRevenue() {
        return revenue;
    }
}
