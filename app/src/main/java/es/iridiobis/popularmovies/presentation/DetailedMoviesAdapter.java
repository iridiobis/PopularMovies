package es.iridiobis.popularmovies.presentation;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.iridiobis.popularmovies.R;
import es.iridiobis.popularmovies.data.api.TheMovieDbImageUriBuilder;
import es.iridiobis.popularmovies.domain.model.Movie;

public class DetailedMoviesAdapter extends RecyclerView.Adapter<DetailedMoviesAdapter.MovieViewHolder> {

    private final List<Movie> movies;
    private final LayoutInflater inflater;
    private final DetailedMoviesFragment.OnFragmentInteractionListener listener;

    public DetailedMoviesAdapter(final Context context, final DetailedMoviesFragment.OnFragmentInteractionListener listener) {
        this.movies = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(
            final ViewGroup parent,
            final int viewType) {
        return new MovieViewHolder(inflater.inflate(R.layout.view_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(
            final MovieViewHolder holder,
            final int position) {

        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(final List<Movie> origin) {
        movies.clear();
        movies.addAll(origin);
        notifyDataSetChanged();

    }

    public final class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Movie movie;
        @BindView(R.id.movie_title)
        TextView titleView;
        @BindView(R.id.movie_year)
        TextView yearView;
        @BindView(R.id.movie_rating)
        TextView ratingView;
        @BindView(R.id.movie_poster)
        ImageView posterView;
        @BindView(R.id.movie_genres)
        TextView genresView;
        
        public MovieViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        
        public void bind(final Movie movie) {
            this.movie = movie;
            titleView.setText(movie.getOriginalTitle());
            yearView.setText(movie.getReleaseDate().substring(0, 4));
            ratingView.setText(String.format(Locale.US, "%.1f (%d)", movie.getVoteAverage(), (long) movie.getPopularity()));
            final Uri posterUrl = TheMovieDbImageUriBuilder.buildW185Image(movie.getPosterPath());
            Picasso.with(itemView.getContext()).load(posterUrl).into(posterView);

            final StringBuilder genres = new StringBuilder();
            for(final String genre : movie.getGenres()) {
                genres.append(genre).append(", ");
            }
            genres.delete(genres.length() - 2, genres.length());
            genresView.setText(genres);
        }

        @Override
        public void onClick(final View v) {
            listener.onFragmentInteraction(movie.getId());
        }
    }
}
