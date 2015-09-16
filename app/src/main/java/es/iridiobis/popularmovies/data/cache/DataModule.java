package es.iridiobis.popularmovies.data.cache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import es.iridiobis.popularmovies.domain.repositories.MoviesRepository;

/**
 * Created by iridio on 16/09/15.
 */
@Module
public class DataModule {
    @Provides
    @Singleton
    public MoviesRepository providesMoviesRepository(final MoviesCache cache) {
        return cache;
    }
}
