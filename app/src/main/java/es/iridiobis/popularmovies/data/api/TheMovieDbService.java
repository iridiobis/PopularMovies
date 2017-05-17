package es.iridiobis.popularmovies.data.api;

import es.iridiobis.popularmovies.domain.model.Movie;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for the services of themoviedb.org
 */
public interface TheMovieDbService {
    @GET("/3/discover/movie")
    Observable<DiscoverMoviesResult> discoverMovies(@Query("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("/3/movie/{movieId}")
    Single<Movie> discoverMovie(@Path("movieId") int movieId, @Query("api_key") String apiKey);
}
