package es.iridiobis.popularmovies.android;

import es.iridiobis.popularmovies.presentation.DetailedMoviesFragment;
import es.iridiobis.popularmovies.presentation.MovieDetailFragment;
import es.iridiobis.popularmovies.presentation.MoviesFragment;

/**
 * Created by iridio on 16/09/15.
 */
public interface PopularMoviesGraph {
    void inject(MoviesFragment moviesFragment);
    void inject(DetailedMoviesFragment fragment);
    void inject(MovieDetailFragment movieDetailFragment);
}
