package es.iridiobis.popularmovies.data.api;

import java.util.List;

import es.iridiobis.popularmovies.domain.model.Movie;

/**
 * Created by iridio on 16/09/15.
 */
public class DiscoverMoviesResult {
    List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }
}
