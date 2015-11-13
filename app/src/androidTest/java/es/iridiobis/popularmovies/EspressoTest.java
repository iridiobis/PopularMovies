package es.iridiobis.popularmovies;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import es.iridiobis.popularmovies.data.api.ApiModule;
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
public class EspressoTest extends InstrumentationTestCase {

    @Rule
    public ActivityTestRule<RootActivity> activityRule
            = new BeforeActivityTestRule<RootActivity>(RootActivity.class, new BeforeActivityTestRule.OnBeforeActivityLaunchedListener<RootActivity>() {
        @Override
        public void beforeActivityLaunched(@NonNull Application application, @NonNull RootActivity activity) {
            Log.i("BLA", "bal");
            server = new MockWebServer();
            ApiModule.BASE_URL = server.url("/").toString();
        }
    });

    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Log.i("BLA", "setup");
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void tearDown() throws Exception {
        Log.i("BLA", "tear");
        server.shutdown();
    }

    @Test
    public void mostPopularOnLaunch() {
        Log.i("BLA", "mp");
        String fileName = "by_popularity_200_ok_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(getInstrumentation().getContext(), fileName)));

        onView(
                allOf(
                        isDescendantOfA(withId(R.id.toolbar)),
                        withText(R.string.nav_by_popularity)
                )
        ).check(matches(isDisplayed()));
    }

    @Test
    public void changeSortingMode() {
        Log.i("BLA", "sort");
        String byPopularityMoviesFile = "by_popularity_200_ok_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(getInstrumentation().getContext(), byPopularityMoviesFile)));
        // Type text and then press the button.
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        String fileName = "by_rating_200_ok_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(getInstrumentation().getContext(), fileName)));
        onView(withText(R.string.nav_by_rating)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());



        onView(
                allOf(
                        isDescendantOfA(withId(R.id.toolbar)),
                        withText(R.string.nav_by_rating)
                )
        ).check(matches(isDisplayed()));
    }

    public static String getStringFromFile(Context context, String filePath) {
        final InputStream stream;
        StringBuilder sb = new StringBuilder();
        try {
            stream = context.getResources().getAssets().open(filePath);

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
