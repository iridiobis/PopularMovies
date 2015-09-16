package es.iridiobis.popularmovies.android;

import javax.inject.Singleton;

import dagger.Component;
import es.iridiobis.popularmovies.data.api.DebugApiModule;
import es.iridiobis.popularmovies.data.cache.DataModule;

/**
 * Created by iridio on 16/09/15.
 */
@Singleton
@Component(modules = {DataModule.class, DebugApiModule.class})
public interface PopularMoviesComponent extends PopularMoviesGraph {
}
