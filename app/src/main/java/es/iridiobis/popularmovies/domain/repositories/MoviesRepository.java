package es.iridiobis.popularmovies.domain.repositories;

import java.util.List;

import es.iridiobis.popularmovies.domain.model.Movie;
import rx.Observable;

/**
 * Created by iridio on 16/09/15.
 */
public interface MoviesRepository {
    Observable<List<Movie>> getMovies(String mode);

    Observable<Movie> getMovie(int movieId);
}
