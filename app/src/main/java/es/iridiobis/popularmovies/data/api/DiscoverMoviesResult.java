package es.iridiobis.popularmovies.data.api;

import java.util.List;

import es.iridiobis.popularmovies.domain.model.Movie;

public class DiscoverMoviesResult {

    List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

}
