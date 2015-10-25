package es.iridiobis.popularmovies.data.cache;

import android.util.SparseArray;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.iridiobis.popularmovies.BuildConfig;
import es.iridiobis.popularmovies.data.api.DiscoverMoviesResult;
import es.iridiobis.popularmovies.data.api.TheMovieDbService;
import es.iridiobis.popularmovies.domain.model.Movie;
import es.iridiobis.popularmovies.domain.repositories.MoviesRepository;
import retrofit.Retrofit;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

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
    public MoviesCache(final Retrofit retrofit) {
        this.service = retrofit.create(TheMovieDbService.class);
        this.movieSparseArray = new SparseArray<>(MOVIES_PER_PAGE);
        this.movies = new ArrayList<>(MOVIES_PER_PAGE);
    }

    @Override
    public Observable<List<Movie>> getMovies(final String mode, final boolean refresh) {
        //TODO define a TTL for the cached data
        if (movieSparseArray.size() == 0) {
            return requestAndCache(mode);
        } else if (refresh) {
            return Observable.concat(
                    Observable.just(ImmutableList.copyOf(movies)),
                    requestAndCache(mode)
            );
        } else {
            return Observable.<List<Movie>>just(ImmutableList.copyOf(movies));
        }
    }

    @Override
    public Observable<Movie> getMovie(final int movieId) {
        final Optional<Movie> movie = Optional.fromNullable(movieSparseArray.get(movieId));
        if (movie.isPresent())
            return Observable.just(movie.get());
        else
            return Observable.empty();
    }

    private Observable<List<Movie>> requestAndCache(final String mode) {
        return service.discoverMovies(mode, BuildConfig.THE_MOVIE_DB_API_KEY)
                .map(new Func1<DiscoverMoviesResult, List<Movie>>() {
                    @Override
                    public List<Movie> call(final DiscoverMoviesResult discoverMoviesResult) {
                        return discoverMoviesResult.getResults();
                    }
                })
                .doOnNext(new Action1<List<Movie>>() {
                    @Override
                    public void call(List<Movie> movies) {
                        MoviesCache.this.movies.clear();
                        MoviesCache.this.movies.addAll(movies);
                        for (Movie movie : movies) {
                            movieSparseArray.put(movie.getId(), movie);
                        }
                    }
                });
    }

}