package es.iridiobis.popularmovies.presentation;

import android.os.Bundle;
import android.view.View;

/**
 * Created by iridio on 16/09/15.
 */
public abstract class Presenter<T extends View> {
    private T view;

    public T getView() {
        return view;
    }

    public boolean hasView() {
        return view != null;
    }

    public void onEnterScope(final Bundle bundle) {
    }

    public final void takeView(final T view) {
        this.view = view;
        onLoad();
    }

    public abstract void onLoad();

    public final void dropView() {
        this.view = null;
    }

    public void onSave(Bundle bundle) {
    }

    public void onExitScope() {
    }
}
