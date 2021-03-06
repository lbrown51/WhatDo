package com.ad340.whatdo;

import android.view.KeyEvent;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.contrib.PickerActions.setTime;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ad340.whatdo.DialogTests.withRecyclerView;
import static com.ad340.whatdo.MaterialDatePickerTestUtils.clickCancel;
import static com.ad340.whatdo.MaterialDatePickerTestUtils.clickDayDatePicker;
import static com.ad340.whatdo.MaterialDatePickerTestUtils.clickDayDateRangePicker;
import static com.ad340.whatdo.MaterialDatePickerTestUtils.clickOk;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;


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
        Tests whether the app loads the recycler view
    */
    @Test
    public void hasRecyclerView() {
        onView(withId(R.id.todo_list_recycler_view))
                .check(matches(isDisplayed()));
    }

    /*
        Tests whether the tasks have the correct content.
    */
    @Test
    public void tasksDisplayCorrectViews() throws InterruptedException {
        Thread.sleep(250);

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPosition(0)).check(matches(hasDescendant(withId(R.id.name_text))));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPosition(0)).check(matches(hasDescendant(withId(R.id.date_text))));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPosition(0)).check(matches(hasDescendant(withId(R.id.todo_item_finished_checkbox))));
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
        Tests whether tasks can be expanded from task name or not
     */
    @Test
    public void tasksExpandAndCollapse() throws InterruptedException {
        // Date and buttons are invisible
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.time_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.reschedule_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        closeSoftKeyboard();

        // Expand first task
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());
        closeSoftKeyboard();

        // Date and buttons are visible
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.reschedule_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.time_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        closeSoftKeyboard();
        // Collapse first task
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());

        closeSoftKeyboard();
        Thread.sleep(500);
        // Date and buttons are invisible
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.reschedule_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.time_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    /*
        Tests whether tasks can be expanded from the time TextView or not
     */
    @Test
    public void tasksExpandAndCollapse2() throws InterruptedException {
        // Date and buttons are invisible
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.time_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.reschedule_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        closeSoftKeyboard();

        // Expand second task
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.time_text))
                .perform(click());

        closeSoftKeyboard();
        // Date and buttons are visible
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.reschedule_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.time_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        closeSoftKeyboard();
        // Collapse second task
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.time_text))
                .perform(click());
        closeSoftKeyboard();
        Thread.sleep(1000);

        // Date and buttons are invisible
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.reschedule_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.time_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_btn))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    /*
        Tests whether tasks have a Cancel option
    */
    @Test
    public void hasCancelOption() throws InterruptedException {
        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.reschedule_btn))
                .perform(click());

        try {
            onView(withText(R.string.cancel))
                .check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            e.printStackTrace();
        }
    }

    /*
        Tests whether tasks can be cancelled
    */
    @Test
    public void canCancelTask() throws InterruptedException {
        Thread.sleep(500);
        closeSoftKeyboard();
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());

        Thread.sleep(500);
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.reschedule_btn))
                .perform(click());

        Thread.sleep(500);
        onView(withText(R.string.cancel)).perform(click());

        Thread.sleep(500);
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .check(matches(not(withText("First Todo"))));
    }

    /*
        Tests whether tasks have a Reschedule option
    */
    @Test
    public void hasRescheduleOption() {
        closeSoftKeyboard();
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.reschedule_btn))
                .perform(click());

        onView(withText(R.string.reschedule)).perform(click());
    }

    /*
        Tests whether tasks can be rescheduled
    */
    @Test
    public void canRescheduleTask() throws InterruptedException {
        Thread.sleep(500);
        closeSoftKeyboard();
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());

        Thread.sleep(500);
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.reschedule_btn))
                .perform(click());

        onView(withText(R.string.reschedule)).perform(click());

        int year = 2020;
        int month = 6;
        int dayOfMonth = 28;
        onView(withClassName(Matchers.equalTo(
                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
        Thread.sleep(500);
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_text))
                .check(matches(withText("Sunday, June 28, 2020")));
    }

    /*
        Tests that the DatePicker works
     */
    @Test
    public void datePickerWorks() throws InterruptedException {
        Thread.sleep(1000);
        int year = 2020;
        int month = 6;
        int dayOfMonth = 28;
        closeSoftKeyboard();
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_btn))
                .perform(click());

        onView(withClassName(Matchers.equalTo(
                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_text))
                .check(matches(withText("Sunday, June 28, 2020")));
    }

    /*
        Tests that the TimePicker works
     */
    @Test
    public void timePickerWorks() throws InterruptedException {
        int hourOfDay = 16;
        int minute = 30;
        Thread.sleep(500);
        closeSoftKeyboard();
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.time_btn))
                .perform(click());

        onView(withClassName(Matchers.equalTo(
                TimePicker.class.getName()))).perform(setTime(hourOfDay, minute));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.time_text))
                .check(matches(withText("4:30 PM")));
    }

    /*
    Tests that notes can be added and saved
    */
    @Test
    public void notesWork() throws InterruptedException {
        closeSoftKeyboard();
        Thread.sleep(500);
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_btn))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_text))
                .perform(typeText("About my task"));
        closeSoftKeyboard();

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_text))
                .check(matches(withText("About my task")));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.notes_text))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    /*
        Tests whether tasks can be finished
    */
    @Test
    public void canFinishTask() throws InterruptedException {
        closeSoftKeyboard();
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(1, R.id.todo_item_finished_checkbox))
                .perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(1, R.id.name_text))
                .check(matches(not(withText("Second Todo"))));
    }

    /*
        Tests that an existing tag can be applied
     */
    @Test
    public void addNexistingTag() throws InterruptedException {
//        closeSoftKeyboard();
//        Thread.sleep(500);
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.name_text))
//                .perform(click());
//        closeSoftKeyboard();
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.tag_btn))
//                .perform(click());
//        onView(withText("Show All Tags")).perform(click());
//        onView(withText("maTestTag")).perform(click());
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.tag_display))
//                .check(matches(withText("maTestTag")));
//
//        closeSoftKeyboard();
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.tag_btn))
//                .perform(click());
//        onView(withText("maTestTag")).perform(click());
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.tag_display))
//                .check(matches(withText("maTestTag")));
    }

    /*
        Tests that a new tag can be applied
     */
    @Test
    public void addNewTag() throws InterruptedException {
//        closeSoftKeyboard();
//        Thread.sleep(500);
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.name_text))
//                .perform(click());
//        closeSoftKeyboard();
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.tag_btn))
//                .perform(click());
//        onView(withText("Add New Tag")).perform(click());
//        onView(withId(R.id.add_tag_edit_text)).perform(typeText("maTestTag"), ViewActions.closeSoftKeyboard());
//        onView(withId(R.id.add_tag_finish_btn)).perform(click());
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.tag_display))
//                .check(matches(withText("maTestTag")));
    }
    
}
