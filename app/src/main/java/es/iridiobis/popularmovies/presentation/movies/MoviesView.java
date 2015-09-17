package es.iridiobis.popularmovies.presentation.movies;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.iridiobis.popularmovies.R;
import es.iridiobis.popularmovies.android.PopularMoviesApplication;
import es.iridiobis.popularmovies.domain.model.Movie;
import es.iridiobis.popularmovies.presentation.MoviesAdapter;

/**
 * Created by iridio on 16/09/15.
 */
public class MoviesView extends SwipeRefreshLayout {

    @Inject
    MoviesPresenter presenter;

    @Bind(R.id.movies_grid)
    GridView moviesGrid;

    private MoviesAdapter adapter;

    public MoviesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            //TODO this has to change, create view (fragment? :S) scoped components that depend on PMC
            PopularMoviesApplication.get(context).getComponent().inject(this);
            setOnRefreshListener(presenter);
            adapter = new MoviesAdapter(context);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.bind(this);
            moviesGrid.setAdapter(adapter);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            presenter.takeView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (!isInEditMode()) {
            presenter.dropView();
        }
        super.onDetachedFromWindow();
    }

    public void showErrorFetchingMovies() {
        Toast.makeText(getContext(), "Error fetching movies", Toast.LENGTH_LONG).show();

    }

    public void setMovies(final List<Movie> movies) {
        adapter.setMovies(movies);
    }

    public int getFirstVisiblePosition() {
        return moviesGrid.getFirstVisiblePosition();
    }

    public void setSelectionFromTop(int firstVisiblePosition) {
        moviesGrid.setSelection(firstVisiblePosition);
    }
}
