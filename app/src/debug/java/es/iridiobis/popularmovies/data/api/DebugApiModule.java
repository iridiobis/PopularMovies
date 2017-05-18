package es.iridiobis.popularmovies.data.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Module(includes = ApiModule.class)
public class DebugApiModule {
    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        final Interceptor logInterceptor = new Interceptor() {
            @Override
            public Response intercept(@NonNull final Chain chain) throws IOException {
                Request request = chain.request();

                long t1 = System.nanoTime();
                Log.i("retrofit", String.format("Sending request %s on %s%n%s",
                        request.url(), chain.connection(), request.headers()));

                Response response = chain.proceed(request);

                long t2 = System.nanoTime();
                Log.i("retrofit", String.format("Received response for %s in %.1fms%n%s",
                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));

                return response;
            }
        };
        return new OkHttpClient().newBuilder()
                .addInterceptor(new StethoInterceptor())
                .addInterceptor(logInterceptor)
                .build();
    }

}
