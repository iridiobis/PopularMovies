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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.iridiobis.popularmovies.R;
import es.iridiobis.popularmovies.android.PopularMoviesApplication;
import es.iridiobis.popularmovies.data.api.TheMovieDbImageUriBuilder;
import es.iridiobis.popularmovies.domain.model.Movie;
import es.iridiobis.popularmovies.domain.repositories.MoviesRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
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
    @Bind(R.id.movie_detail_title)
    TextView titleView;
    @Bind(R.id.movie_detail_year)
    TextView yearView;
    @Bind(R.id.movie_detail_rating)
    TextView ratingView;
    @Bind(R.id.movie_detail_overview)
    TextView overviewView;
    @Bind(R.id.movie_detail_poster)
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
        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
//        }

        return rootView;
    }

    private void discoverMovie(final int movieId) {

        final Action1<Movie> onNext = new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                toolbarLayout.setTitle(movie.getOriginalTitle());
                toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

                titleView.setText(movie.getOriginalTitle());
                yearView.setText(movie.getReleaseDate());
                //TODO make constant or res
                ratingView.setText(String.format("%d/10", (long)movie.getVoteAverage()));
                overviewView.setText(movie.getOverview());
                final Uri backdropUrl = TheMovieDbImageUriBuilder.buildW500Image(movie.getPosterPath());
                Picasso.with(getActivity()).load(backdropUrl).into(backdropView);
                final Uri posterUrl = TheMovieDbImageUriBuilder.buildW185Image(movie.getPosterPath());
                Picasso.with(getActivity()).load(posterUrl).into(posterView);
            }
        };

        final Action1<Throwable> onError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //TODO setRefreshing(false);
                showErrorFetchingMovie();
            }
        };

        final Action0 onComplete = new Action0() {
            @Override
            public void call() {
                //TODO setRefreshing(false);
            }
        };

        //TODO setRefreshing(true);

        repository.getMovie(movieId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError, onComplete);
    }

    private void showErrorFetchingMovie() {
        Toast.makeText(getActivity(), "Error fetching movie", Toast.LENGTH_LONG).show();
    }

}
