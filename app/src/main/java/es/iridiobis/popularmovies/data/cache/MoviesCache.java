package es.iridiobis.popularmovies.data.cache;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.iridiobis.popularmovies.BuildConfig;
import es.iridiobis.popularmovies.data.api.DiscoverMoviesResult;
import es.iridiobis.popularmovies.data.api.TheMovieDbService;
import es.iridiobis.popularmovies.domain.model.Movie;
import es.iridiobis.popularmovies.domain.repositories.MoviesRepository;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Implementation of the {@link MoviesRepository} that uses a SparseArray to cache the movies
 * and uses Retrofit to access the themoviedb api
 */
@Singleton
public class MoviesCache implements MoviesRepository {
    private static final int MOVIES_PER_PAGE = 20;
    /**
     * The movies list contains the ORDERED list of movies.
     */
    private final List<Movie> movies;
    /**
     * Sparse array to access the movies by id
     */
    private final SparseArray<Movie> movieSparseArray;
    private final TheMovieDbService service;

    @Inject
    public MoviesCache(final TheMovieDbService service) {
        this.service = service;
        this.movieSparseArray = new SparseArray<>(MOVIES_PER_PAGE);
        this.movies = new ArrayList<>(MOVIES_PER_PAGE);
    }

    @Override
    public Observable<List<Movie>> getMovies(final String mode, final boolean refresh) {
        if (movieSparseArray.size() == 0) {
            return requestAndCache(mode);
        } else if (refresh) {
            return Observable.concat(
                    Observable.just(Collections.unmodifiableList(movies)),
                    requestAndCache(mode)
            );
        } else {
            return Observable.just(Collections.unmodifiableList(movies));
        }
    }

    @Override
    public Single<Movie> getMovie(final int movieId) {
        final Movie movie = movieSparseArray.get(movieId);
        if (movie == null)
            return service.discoverMovie(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        else
            return Single.just(movie);
    }

    private Observable<List<Movie>> requestAndCache(final String mode) {
        return service.discoverMovies(mode, BuildConfig.THE_MOVIE_DB_API_KEY)
                .map(new Function<DiscoverMoviesResult, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@NonNull final DiscoverMoviesResult discoverMoviesResult) throws Exception {
                        return discoverMoviesResult.getResults();
                    }
                })
                .doOnNext(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(@NonNull final List<Movie> movies) throws Exception {
                        MoviesCache.this.movies.clear();
                        MoviesCache.this.movies.addAll(movies);
                        for (Movie movie : movies) {
                            movieSparseArray.put(movie.getId(), movie);
                        }
                    }
                });
    }

}
