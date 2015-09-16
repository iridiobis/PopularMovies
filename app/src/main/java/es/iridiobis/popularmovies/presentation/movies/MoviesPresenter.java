package es.iridiobis.popularmovies.presentation.movies;

import android.support.v4.widget.SwipeRefreshLayout;

import java.util.List;

import javax.inject.Inject;

import es.iridiobis.popularmovies.domain.model.Movie;
import es.iridiobis.popularmovies.domain.repositories.MovieDiscoveryMode;
import es.iridiobis.popularmovies.domain.repositories.MoviesRepository;
import es.iridiobis.popularmovies.presentation.Presenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by iridio on 16/09/15.
 */
public class MoviesPresenter extends Presenter<MoviesView> implements SwipeRefreshLayout.OnRefreshListener {

    private final MoviesRepository repository;

    @Inject
    public MoviesPresenter(MoviesRepository repository) {
        this.repository = repository;
    }


    @Override
    public void onLoad() {
        discoverMovies(false);
    }

    @Override
    public void onRefresh() {
        if (hasView()) discoverMovies(true);
    }

    private void discoverMovies(final boolean refresh) {

        final Action1<List<Movie>> onNext = new Action1<List<Movie>>() {
            @Override
            public void call(List<Movie> movies) {
                if (hasView()) getView().setMovies(movies);
            }
        };

        final Action1<Throwable> onError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (hasView()) {
                    getView().setRefreshing(false);
                    getView().showErrorFetchingMovies();
                }
            }
        };

        final Action0 onComplete = new Action0() {
            @Override
            public void call() {
                if (hasView()) getView().setRefreshing(false);
            }
        };

        getView().setRefreshing(true);

        repository.getMovies(MovieDiscoveryMode.POPULARITY, refresh)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError, onComplete);
    }
}
