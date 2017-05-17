package es.iridiobis.popularmovies.presentation;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.iridiobis.popularmovies.R;
import es.iridiobis.popularmovies.android.PopularMoviesApplication;
import es.iridiobis.popularmovies.data.api.TheMovieDbImageUriBuilder;
import es.iridiobis.popularmovies.domain.model.Movie;
import es.iridiobis.popularmovies.domain.repositories.MoviesRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_MOVIE_ID = "movie_id";
    public static final int NO_MOVIE = -1;
    //TODO create class to handle images urls and sizes
    private final static String BACKDROP_URL_ROOT = "http://image.tmdb.org/t/p/w500/%s";


    @Inject
    MoviesRepository repository;

    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.movie_detail_title)
    TextView titleView;
    @BindView(R.id.movie_detail_year)
    TextView yearView;
    @BindView(R.id.movie_detail_rating)
    TextView ratingView;
    @BindView(R.id.movie_detail_overview)
    TextView overviewView;
    @BindView(R.id.movie_detail_poster)
    ImageView posterView;

    ImageView backdropView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movieId Id of the movie to display
     * @return A new instance of fragment MovieDetailFragment.
     */
    public static MovieDetailFragment newInstance(final int movieId) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PopularMoviesApplication.get(getActivity()).getComponent().inject(this);

        if (getArguments().containsKey(ARG_MOVIE_ID)) {
            discoverMovie(getArguments().getInt(ARG_MOVIE_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);
        toolbarLayout = ButterKnife.findById(getActivity(), R.id.toolbar_layout);
        backdropView = ButterKnife.findById(getActivity(), R.id.movie_detail_backdrop);

        return rootView;
    }

    private void discoverMovie(final int movieId) {

        final Consumer<Movie> onNext = new Consumer<Movie>() {
            @Override
            public void accept(Movie movie) {
                toolbarLayout.setTitle(movie.getOriginalTitle());
                toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

                titleView.setText(movie.getOriginalTitle());
                yearView.setText(movie.getReleaseDate().substring(0, 4));
                ratingView.setText(String.format(Locale.US, "%.1f (%d)", movie.getVoteAverage(), (long) movie.getPopularity()));
                overviewView.setText(movie.getOverview());
                final Uri backdropUrl = TheMovieDbImageUriBuilder.buildW500Image(movie.getPosterPath());
                Picasso.with(getActivity()).load(backdropUrl).into(backdropView);
                final Uri posterUrl = TheMovieDbImageUriBuilder.buildW185Image(movie.getPosterPath());
                Picasso.with(getActivity()).load(posterUrl).into(posterView);
            }
        };

        final Consumer<Throwable> onError = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                showErrorFetchingMovie();
            }
        };

        repository.getMovie(movieId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    private void showErrorFetchingMovie() {
        Toast.makeText(getActivity(), "Error fetching movie", Toast.LENGTH_LONG).show();
    }

}
