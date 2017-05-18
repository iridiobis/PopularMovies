package es.iridiobis.popularmovies.android;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class PopularMoviesApplication extends Application {
    private PopularMoviesComponent component;

    public static PopularMoviesApplication get(final Context context) {
        return (PopularMoviesApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerPopularMoviesComponent.builder().build();
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

    }

    public PopularMoviesComponent getComponent() {
        return component;
    }
}
