package es.iridiobis.popularmovies.presentation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.iridiobis.popularmovies.domain.model.Movie;

/**
 * Created by iridio on 16/09/15.
 */
public class MoviesAdapter extends BaseAdapter {

    private final static String URL_ROOT = "http://image.tmdb.org/t/p/w185/%s";

    private final Context context;
    private final List<Movie> movies;

    public MoviesAdapter(final Context context) {
        this.context = context;
        //not the best move, but...
        this.movies = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(final int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return movies.get(position).getId();
    }

    @Override
    public View getView(final int position, final View view, final ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) view;
        }

        final String url = String.format(URL_ROOT, movies.get(position).getPosterPath());

        Picasso.with(context).load(url).into(imageView);
        return imageView;
    }

    public void setMovies(final List<Movie> origin) {
        movies.clear();
        movies.addAll(origin);
        notifyDataSetChanged();

    }
}
