package com.ad340.whatdo;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.appbar.MaterialToolbar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Locale;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /*
        Tests whether the app bar is displayed
     */
    @Test
    public void hasHeader() {
        onView(withId(R.id.top_app_bar))
                .check(matches(isDisplayed()));
    }

    /*
        Tests whether the header has the correct date
     */
    @Test
    public void headerTextUpdates() {
        AppCompatActivity activity = (AppCompatActivity) TestUtils.getActivity();
        Calendar today = Calendar.getInstance();
        StringBuilder expectedText = new StringBuilder(TestUtils.getString(R.string.app_bar_title));
        expectedText.append("      ");
        expectedText.append(today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        expectedText.append(String.format(" %02d, %04d",
                today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.YEAR)));
        MaterialToolbar appBar = activity.findViewById(R.id.top_app_bar);
        String displayText = appBar.getTitle().toString();
        Log.e("displayText", displayText);
        Log.e("expectedText", expectedText.toString());
        assertEquals(displayText, expectedText.toString());
    }

    /*
        Tests whether the app loads the recycler view
    */
    @Test
    public void hasRecyclerView() {
        onView(withId(R.id.todo_list_recycler_view))
                .check(matches(isDisplayed()));
    }

    /*
        Tests whether the recycler view has at least one of the tasks
        we expect it to.
    */
    @Test
    public void hasAtLeastOneTask() {
        onView(withId(R.id.todo_list_recycler_view))
                .check(matches(hasDescendant(withText("Test"))));
    }
}