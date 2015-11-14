package es.iridiobis.popularmovies.data.api;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by iridio on 16/09/15.
 */
@Module
public class ApiModule {
    public static String BASE_URL = "http://api.themoviedb.org";
    @Provides
    public TheMovieDbService provideRetrofit(final OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(TheMovieDbService.class);
    }
}
