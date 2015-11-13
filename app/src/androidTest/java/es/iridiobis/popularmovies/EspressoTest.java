package es.iridiobis.popularmovies;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.iridiobis.popularmovies.presentation.RootActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {

    @Rule
    public ActivityTestRule<RootActivity> activityRule = new ActivityTestRule<>(
            RootActivity.class);

    @Test
    public void mostPopularOnLaunch() {
        onView(
                allOf(
                        isDescendantOfA(withId(R.id.toolbar)),
                        withText(R.string.nav_by_popularity)
                )
        ).check(matches(isDisplayed()));
    }

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_by_rating)).perform(click());
    }
}
