package es.iridiobis.popularmovies.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by iridio on 16/09/15.
 */
public class PopularMoviesApplication extends Application {
    private PopularMoviesComponent component;

    public static PopularMoviesApplication get(final Context context) {
        return (PopularMoviesApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerPopularMoviesComponent.builder().build();
    }

    public PopularMoviesComponent getComponent() {
        return component;
    }
}
