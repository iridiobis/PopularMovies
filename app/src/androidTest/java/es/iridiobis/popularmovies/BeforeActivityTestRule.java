package es.iridiobis.popularmovies;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

/**
 * {@link ActivityTestRule} which provides hook for
 * {@link ActivityTestRule#beforeActivityLaunched()} method. Can be used for test dependency
 * injection especially in Espresso based tests.
 *
 * @author Tomasz Rozbicki
 */
public class BeforeActivityTestRule<T extends Activity> extends ActivityTestRule<T> {

    private final OnBeforeActivityLaunchedListener<T> mListener;

    public BeforeActivityTestRule(Class<T> activityClass,
                                  @NonNull OnBeforeActivityLaunchedListener<T> listener) {
        this(activityClass, false, listener);
    }

    public BeforeActivityTestRule(Class<T> activityClass, boolean initialTouchMode,
                                  @NonNull OnBeforeActivityLaunchedListener<T> listener) {
        this(activityClass, initialTouchMode, true, listener);
    }

    public BeforeActivityTestRule(Class<T> activityClass, boolean initialTouchMode,
                                  boolean launchActivity,
                                  @NonNull OnBeforeActivityLaunchedListener<T> listener) {
        super(activityClass, initialTouchMode, launchActivity);
        mListener = listener;
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        mListener.beforeActivityLaunched((Application) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext(), getActivity());
    }

    public interface OnBeforeActivityLaunchedListener<T> {

        void beforeActivityLaunched(@NonNull Application application, @NonNull T activity);
    }
}
