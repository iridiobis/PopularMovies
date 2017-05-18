package es.iridiobis.popularmovies.presentation;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedMoviesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailedMoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedMoviesFragment extends Fragment {

    private static final String ARG_FIRST_VISIBLE_POSITION = "first_visible_position";
    private static final String ARG_DISCOVERY_MODE = "discovery_mode";
    @Inject
    MoviesRepository repository;
    @BindView(R.id.progress)
    View progress;
    @BindView(R.id.movies_list)
    RecyclerView moviesList;
    private String discoveryMode = MovieDiscoveryMode.POPULARITY;
    private DetailedMoviesAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public DetailedMoviesFragment() {
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
    public static DetailedMoviesFragment newInstance(
            final String mode,
            final int firstVisiblePosition) {
        DetailedMoviesFragment fragment = new DetailedMoviesFragment();
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
        }
        setHasOptionsMenu(true);
        getActivity().setTitle(
                MovieDiscoveryMode.POPULARITY.equals(discoveryMode)
                        ? R.string.nav_by_popularity
                        : R.string.nav_by_rating
        );
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_detailed_movies, container, false);
        ButterKnife.bind(this, view);
        adapter = new DetailedMoviesAdapter(getActivity(), mListener);
        final LinearLayoutManager transactionOverviewLinearLayoutManager = new LinearLayoutManager(getActivity());
        moviesList.setAdapter(adapter);
        moviesList.setLayoutManager(transactionOverviewLinearLayoutManager);

        progress.setVisibility(View.VISIBLE);
        discoverMovies(false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(
            final Menu menu,
            final MenuInflater inflater) {

        inflater.inflate(R.menu.root, menu);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_refresh) {
            progress.setVisibility(View.VISIBLE);
            discoverMovies(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) getActivity();
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
                progress.setVisibility(View.INVISIBLE);
                if (refresh) Toast.makeText(getActivity(), "Movies refreshed", Toast.LENGTH_SHORT).show();
            }
        };

        final Consumer<Throwable> onError = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                showErrorFetchingMovies();
                progress.setVisibility(View.INVISIBLE);
            }
        };

        repository.getMovies(discoveryMode, refresh)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
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

        void onFragmentInteraction(int selectedMovieId);
    }

}
