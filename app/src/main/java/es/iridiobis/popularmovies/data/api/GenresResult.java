package es.iridiobis.popularmovies.data.api;

import java.util.List;

public class GenresResult {

    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(final List<Genre> genres) {
        this.genres = genres;
    }

}
