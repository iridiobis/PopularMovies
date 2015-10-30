package es.iridiobis.popularmovies.presentation;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import es.iridiobis.popularmovies.R;
import es.iridiobis.popularmovies.android.PopularMoviesApplication;
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

    @Inject
    MoviesRepository repository;

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
     * @param movieId              Id of the movie to display
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
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

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
                Activity activity = getActivity();
                CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(movie.getOriginalTitle());
                }
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
