package es.iridiobis.popularmovies.presentation;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.iridiobis.popularmovies.R;
import es.iridiobis.popularmovies.android.PopularMoviesApplication;
import es.iridiobis.popularmovies.domain.model.Movie;
import es.iridiobis.popularmovies.domain.repositories.MovieDiscoveryMode;
import es.iridiobis.popularmovies.domain.repositories.MoviesRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment {
    private static final String ARG_FIRST_VISIBLE_POSITION = "first_visible_position";
    private static final String ARG_DISCOVERY_MODE = "discovery_mode";
    @Inject
    MoviesRepository repository;
    @BindView(R.id.movies_grid)
    GridView moviesGrid;
    private String discoveryMode = MovieDiscoveryMode.POPULARITY;
    private int firstVisiblePosition;
    private MoviesAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mode                 Discovery mode: popularity, rating
     * @param firstVisiblePosition First of the displayed movies to be visible.
     * @return A new instance of fragment MoviesFragment.
     */
    public static MoviesFragment newInstance(final String mode, final int firstVisiblePosition) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DISCOVERY_MODE, mode);
        args.putInt(ARG_FIRST_VISIBLE_POSITION, firstVisiblePosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PopularMoviesApplication.get(getActivity()).getComponent().inject(this);
        if (getArguments() != null) {
            discoveryMode = getArguments().getString(ARG_DISCOVERY_MODE);
            firstVisiblePosition = getArguments().getInt(ARG_FIRST_VISIBLE_POSITION);
        }
        getActivity().setTitle(
                MovieDiscoveryMode.POPULARITY.equals(discoveryMode)
                        ? R.string.nav_by_popularity
                        : R.string.nav_by_rating
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);
        adapter = new MoviesAdapter(getActivity());
        moviesGrid.setAdapter(adapter);
        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.onFragmentInteraction(((Movie) adapter.getItem(i)).getId());
            }
        });
        discoverMovies(true);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        final int firstVisible = moviesGrid.getFirstVisiblePosition();
        getArguments().putInt(ARG_FIRST_VISIBLE_POSITION, firstVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showErrorFetchingMovies() {
        Toast.makeText(getActivity(), "Error fetching movies", Toast.LENGTH_LONG).show();
    }

    private void discoverMovies(final boolean refresh) {

        final Consumer<List<Movie>> onNext = new Consumer<List<Movie>>() {
            @Override
            public void accept(List<Movie> movies) {
                adapter.setMovies(movies);
                moviesGrid.setSelection(firstVisiblePosition);
            }
        };

        final Consumer<Throwable> onError = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                //TODO setRefreshing(false);
                showErrorFetchingMovies();
            }
        };

        final Action onComplete = new Action() {
            @Override
            public void run() {
                //TODO setRefreshing(false);
            }
        };

        //TODO setRefreshing(true);

        repository.getMovies(discoveryMode, refresh)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError, onComplete);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(int selectedMovieId);
    }

}
