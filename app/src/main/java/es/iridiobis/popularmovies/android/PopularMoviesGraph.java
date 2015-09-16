package es.iridiobis.popularmovies.android;

import es.iridiobis.popularmovies.presentation.MainActivity;
import es.iridiobis.popularmovies.presentation.movies.MoviesView;

/**
 * Created by iridio on 16/09/15.
 */
public interface PopularMoviesGraph {
    //TODO this is not its place!
    void inject(MainActivity activity);

    void inject(MoviesView view);
}
