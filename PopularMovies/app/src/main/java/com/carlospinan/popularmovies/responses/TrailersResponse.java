package com.carlospinan.popularmovies.responses;

import com.carlospinan.popularmovies.models.TrailerModel;

import java.util.List;

/**
 * @author Carlos Piñan
 */
public class TrailersResponse {

    private int id;
    private List<TrailerModel> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TrailerModel> getResults() {
        return results;
    }

    public void setResults(List<TrailerModel> results) {
        this.results = results;
    }
}
