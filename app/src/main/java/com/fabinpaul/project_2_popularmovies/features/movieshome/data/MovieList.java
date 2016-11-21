package com.fabinpaul.project_1_popularmovies.features.movieshome.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabin Paul on 11/5/2016.
 */

public class MovieList {

    private int page;
    private List<Movie> results = new ArrayList<>();
    private int total_results;
    private int total_pages;

    public int getPage() {
        return page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public int getTotalResults() {
        return total_results;
    }

    public int getTotalPages() {
        return total_pages;
    }

    public void updateMovieList(MovieList pMovieList) {
        if (pMovieList.getPage() > page) {
            results.addAll(pMovieList.getResults());
            total_pages = pMovieList.getTotalPages();
            total_results = pMovieList.getTotalResults();
            page = pMovieList.getPage();
        }
    }

    public void clear() {
        results.clear();
        page = 0;
        total_results = 0;
        total_pages = 0;
    }
}
