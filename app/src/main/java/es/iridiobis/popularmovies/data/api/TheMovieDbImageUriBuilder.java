package es.iridiobis.popularmovies.data.api;

import android.net.Uri;

/**
 * Created by iridio on 01/11/15.
 */
public final class TheMovieDbImageUriBuilder {
    private static final String URL_ROOT = "http://image.tmdb.org/t/p/w%d/%s";
    private static final int W185_WIDTH = 185;
    private static final int W500_WIDTH = 500;

    private TheMovieDbImageUriBuilder() {
        //Private constructor, utility class
    }

    public static Uri buildW185Image(final String image) {
        return Uri.parse(String.format(URL_ROOT, W185_WIDTH, image));
    }

    public static Uri buildW500Image(final String image) {
        return Uri.parse(String.format(URL_ROOT, W500_WIDTH, image));
    }
}
