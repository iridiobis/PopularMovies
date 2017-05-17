package es.iridiobis.popularmovies.android;

import javax.inject.Singleton;

import dagger.Component;
import es.iridiobis.popularmovies.data.api.DebugApiModule;
import es.iridiobis.popularmovies.data.cache.DataModule;

@Singleton
@Component(modules = {DataModule.class, DebugApiModule.class})
public interface PopularMoviesComponent extends PopularMoviesGraph {
}
