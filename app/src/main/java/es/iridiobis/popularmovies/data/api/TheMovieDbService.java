package es.iridiobis.popularmovies.data.api;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Retrofit interface for the services of themoviedb.org
 */
public interface TheMovieDbService {
    @GET("/3/discover/movie")
    Observable<DiscoverMoviesResult> discoverMovies(@Query("sort_by") String sortBy, @Query("api_key") String apiKey);
}
