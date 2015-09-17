package es.iridiobis.popularmovies.presentation;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.iridiobis.popularmovies.R;
import es.iridiobis.popularmovies.android.PopularMoviesApplication;
import es.iridiobis.popularmovies.domain.repositories.MovieDiscoveryMode;
import es.iridiobis.popularmovies.presentation.movies.MoviesPresenter;

public class MainActivity extends AppCompatActivity {

    //TODO Would be better to access the presenter through the view? Or just use eventbus and remove this coupling?
    @Inject
    MoviesPresenter presenter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.movies_grid)
    GridView movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO this has to change, create view scoped components that depend on PMC
        PopularMoviesApplication.get(this).getComponent().inject(this);
        if (savedInstanceState != null) {
            //...
            presenter.onEnterScope(savedInstanceState);
        }
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSave(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setSortMode(menu.findItem(R.id.sorting_mode), presenter.getSortMode());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sorting_mode) {
            final String mode = item.isChecked() ? MovieDiscoveryMode.POPULARITY : MovieDiscoveryMode.RATING;
            setSortMode(item, mode);
            presenter.setSortMode(mode);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setSortMode(final MenuItem item, final String sortMode) {
        if (MovieDiscoveryMode.RATING.equals(sortMode)) {
            item.setChecked(true);
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_popularity));
            item.setTitle(getString(R.string.sort_by_popularity));
        } else {
            item.setChecked(false);
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_rating));
            item.setTitle(getString(R.string.sort_by_rating));
        }
    }
}
