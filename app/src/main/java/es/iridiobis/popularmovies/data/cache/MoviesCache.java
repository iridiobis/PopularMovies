package es.iridiobis.popularmovies.data.cache;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.iridiobis.popularmovies.BuildConfig;
import es.iridiobis.popularmovies.data.api.DiscoverMoviesResult;
import es.iridiobis.popularmovies.data.api.Genre;
import es.iridiobis.popularmovies.data.api.GenresResult;
import es.iridiobis.popularmovies.data.api.TheMovieDbService;
import es.iridiobis.popularmovies.domain.model.Movie;
import es.iridiobis.popularmovies.domain.repositories.MovieDiscoveryMode;
import es.iridiobis.popularmovies.domain.repositories.MoviesRepository;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
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
    private final SparseArray<String> genres;
    private final TheMovieDbService service;
    private String lastMode;

    @Inject
    public MoviesCache(final TheMovieDbService service) {
        this.service = service;
        this.movieSparseArray = new SparseArray<>(MOVIES_PER_PAGE);
        this.genres = new SparseArray<>();
        this.movies = new ArrayList<>(MOVIES_PER_PAGE);
    }

    @Override
    public Observable<List<Movie>> getMovies(
            @NonNull final String mode,
            final boolean refresh) {
        if (movieSparseArray.size() == 0) {
            return checkGenres().andThen(requestAndCache(mode));
        } else if (refresh || !mode.equals(lastMode)) {
            return Observable.concat(
                    Observable.just(Collections.unmodifiableList(movies)),
                    requestAndCache(mode)
            );
        } else {
            return Observable.just(Collections.unmodifiableList(movies));
        }
    }

    private Completable checkGenres() {
        if (genres.size() > 0) {
            return Completable.complete();
        } else {
            return service.discoverGenres(BuildConfig.THE_MOVIE_DB_API_KEY)
                    .flatMapCompletable(new Function<GenresResult, CompletableSource>() {
                        @Override
                        public CompletableSource apply(@io.reactivex.annotations.NonNull final GenresResult genresResult) throws Exception {
                            genres.clear();
                            for (final Genre genre : genresResult.getGenres()) {
                                genres.put(genre.getId(), genre.getName());
                            }
                            return Completable.complete();
                        }
                    });
        }
    }

    @Override
    public Single<Movie> getMovie(final int movieId) {
        final Movie movie = movieSparseArray.get(movieId);
        if (movie == null) {
            return service.discoverMovie(movieId, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .flatMap(new Function<Movie, SingleSource<? extends Movie>>() {
                        @Override
                        public SingleSource<? extends Movie> apply(@io.reactivex.annotations.NonNull final Movie movie) throws Exception {
                            return getGenres(movie);
                        }
                    });
        } else {
            return Single.just(movie);
        }
    }

    private Single<Movie> getGenres(final Movie movie) {
        if (genres.size() > 0) {
            final List<String> result = new ArrayList<>(movie.getGenreIds().size());
            for (final int genreId : movie.getGenreIds()) {
                result.add(genres.get(genreId));
            }
            movie.setGenres(result);
            return Single.just(movie);
        } else {
            return service.discoverGenres(BuildConfig.THE_MOVIE_DB_API_KEY)
                    .flatMap(new Function<GenresResult, SingleSource<? extends Movie>>() {
                        @Override
                        public SingleSource<? extends Movie> apply(@io.reactivex.annotations.NonNull final GenresResult genresResult) throws
                                Exception {
                            genres.clear();
                            for (final Genre genre : genresResult.getGenres()) {
                                genres.put(genre.getId(), genre.getName());
                            }
                            movie.setGenres(generateGenres(movie));
                            return Single.just(movie);
                        }
                    });
        }
    }

    private Observable<List<Movie>> requestAndCache(final String mode) {
        final Integer minCount = MovieDiscoveryMode.RATING.equals(mode) ? 1000 : null;
        return service.discoverMovies(mode, minCount, BuildConfig.THE_MOVIE_DB_API_KEY)
                .map(new Function<DiscoverMoviesResult, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@NonNull final DiscoverMoviesResult discoverMoviesResult) throws Exception {
                        final List<Movie> movies = discoverMoviesResult.getResults();
                        lastMode = mode;
                        MoviesCache.this.movies.clear();
                        MoviesCache.this.movies.addAll(movies);
                        for (Movie movie : movies) {
                            movieSparseArray.put(movie.getId(), movie);
                            movie.setGenres(generateGenres(movie));
                        }
                        return movies;
                    }
                });
    }

    private List<String> generateGenres(final Movie movie) {
        final List<String> result = new ArrayList<>(movie.getGenreIds().size());
        for (final int genreId : movie.getGenreIds()) {
            result.add(genres.get(genreId));
        }
        return result;
    }

}
