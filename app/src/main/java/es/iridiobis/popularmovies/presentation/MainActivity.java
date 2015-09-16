package es.iridiobis.popularmovies.presentation;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.iridiobis.popularmovies.R;
import es.iridiobis.popularmovies.android.PopularMoviesApplication;
import es.iridiobis.popularmovies.domain.model.Movie;
import es.iridiobis.popularmovies.domain.repositories.MovieDiscoveryMode;
import es.iridiobis.popularmovies.domain.repositories.MoviesRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    MoviesRepository repository;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.movies_grid)
    GridView moviesGrid;

    private MoviesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //TODO this has to change, create activity scoped components that depend on PMC
        PopularMoviesApplication.get(this).getComponent().inject(this);

        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new MoviesAdapter(this);
        moviesGrid.setAdapter(adapter);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                discoverMovies();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        discoverMovies();
    }

    private void discoverMovies() {

        final Action1<List<Movie>> onNext = new Action1<List<Movie>>() {
            @Override
            public void call(List<Movie> movies) {
                adapter.setMovies(movies);
            }
        };

        final Action1<Throwable> onError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(MainActivity.this, "Error fetching movies", Toast.LENGTH_LONG).show();
            }
        };

        final Action0 onComplete = new Action0() {
            @Override
            public void call() {
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        swipeRefreshLayout.setRefreshing(true);

        repository.getMovies(MovieDiscoveryMode.POPULARITY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError, onComplete);
    }
}
