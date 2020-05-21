package com.ad340.whatdo;

import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.appbar.MaterialToolbar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Locale;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);


    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

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
                .check(matches(hasDescendant(withText("First Todo"))));
    }

    /*
        Tests whether the tasks have the correct content.
    */
    @Test
    public void tasksDisplayCorrectViews() {
        onView(withId(R.id.todo_list_recycler_view))
                .check(matches(hasDescendant(withId(R.id.todo_item_task_name))));

        onView(withId(R.id.todo_list_recycler_view))
                .check(matches(hasDescendant(withId(R.id.todo_item_due_datetime))));

        onView(withId(R.id.todo_list_recycler_view))
                .check(matches(hasDescendant(withId(R.id.todo_item_finished_checkbox))));
    }

    /*
        Tests whether the floating action button is displayed.
    */
    @Test
    public void hasFloatingActionButton() {
        onView(withId(R.id.fab))
                .check(matches(isDisplayed()));
    }

    /*
        Tests whether the floating action button opens a dialog on click,
        and the x button in the dialog closes the dialog
     */
    @Test
    public void openCloseDialog() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.create_todo_dialog))
                .check(matches(isDisplayed()));
        onView(withId(R.id.close_dialog)).perform(click());
        try {
            onView(withId(R.id.create_todo_dialog))
                    .check(matches(not(isDisplayed())));
            onView(withId(R.id.placeholder_text))
                    .check(matches(withText(R.string.placeholder_text)));
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.fab))
                    .check(matches(isDisplayed()));
        }
    }

    /*
        Tests whether the floating action button opens a dialog on click,
        and the x button in the dialog closes the dialog
     */
    @Test
    public void openCloseDialogWithButton() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.create_todo_dialog))
                .check(matches(isDisplayed()));
        onView(withId(R.id.close_dialog)).perform(click());
        try {
            onView(withId(R.id.create_todo_dialog))
                    .check(matches(not(isDisplayed())));
            onView(withId(R.id.placeholder_text))
                    .check(matches(withText(R.string.placeholder_text)));
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.fab))
                    .check(matches(isDisplayed()));
        }
    }

    /*
    Tests whether the create to-do dialog closes with back button
 */
    @Test
    public void openCloseDialogWithBackKey() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.create_todo_dialog))
                .check(matches(isDisplayed()));
        pressKey(KeyEvent.KEYCODE_0);
        Espresso.pressBack();
        try {
            onView(withId(R.id.create_todo_dialog))
                    .check(matches(not(isDisplayed())));
            onView(withId(R.id.placeholder_text))
                    .check(matches(withText(R.string.placeholder_text)));
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.fab))
                    .check(matches(isDisplayed()));
        }
    }

    /*
        Tests whether tasks can be expanded or not
     */
    @Test
    public void tasksExpandAndCollapse() {
        // Date, time, and button are invisible
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.set_date_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.set_time_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.task_submit_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        // Expand fifth task
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.todo_item_task_name))
                .perform(click());

        // Date, time, and button are visible
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.set_date_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.set_time_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.task_submit_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Collapse fifth task
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.todo_item_task_name))
                .perform(click());

        // Date, time, and button are invisible
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.set_date_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.set_time_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(4, R.id.task_submit_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    /*
        Tests whether tasks have a Mark Complete option

    */
    @Test
    public void hasMarkCompleteOption() {
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(2, R.id.todo_item_task_name))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(2, R.id.task_submit_btn))
                .perform(click());

        onView(withText(R.string.mark_complete)).perform(click());
    }


    /*
    Tests whether tasks have a Cancel option

    */
    @Test
    public void hasCancelOption() {
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(2, R.id.todo_item_task_name))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(2, R.id.task_submit_btn))
                .perform(click());

        onView(withText(R.string.cancel)).perform(click());
    }


    /*
    Tests whether tasks have a Reschedule option

    */
    @Test
    public void hasRescheduleOption() {
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(2, R.id.todo_item_task_name))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(2, R.id.task_submit_btn))
                .perform(click());

        onView(withText(R.string.reschedule)).perform(click());
    }
}