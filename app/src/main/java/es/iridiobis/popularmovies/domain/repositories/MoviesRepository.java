package es.iridiobis.popularmovies.domain.repositories;

import java.util.List;

import es.iridiobis.popularmovies.domain.model.Movie;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface MoviesRepository {

    Observable<List<Movie>> getMovies(String mode, boolean refresh);

    Single<Movie> getMovie(int movieId);

}
