package es.iridiobis.popularmovies.data.cache;

import android.util.SparseArray;

import com.google.common.base.Optional;

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
    private final SparseArray<Movie> movieSparseArray;
    private final TheMovieDbService service;

    @Inject
    public MoviesCache(final Retrofit retrofit) {
        this.service = retrofit.create(TheMovieDbService.class);
        this.movieSparseArray = new SparseArray<>(MOVIES_PER_PAGE);
    }

    @Override
    public Observable<List<Movie>> getMovies(final String mode) {
        if (movieSparseArray.size() == 0) {
            return requestAndCache(mode);
        } else {
            return Observable.concat(
                    Observable.just(asList(movieSparseArray)),
                    requestAndCache(mode)
            );
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
                        for (Movie movie : movies) {
                            movieSparseArray.put(movie.getId(), movie);
                        }
                    }
                })
                        //Simplest error policy
                .onErrorResumeNext(Observable.<List<Movie>>empty());
    }

    private List<Movie> asList(SparseArray<Movie> sparseArray) {
        if (sparseArray == null) return null;
        final List<Movie> arrayList = new ArrayList<>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }
}
