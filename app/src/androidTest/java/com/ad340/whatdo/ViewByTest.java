package com.ad340.whatdo;

import android.widget.DatePicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.ad340.whatdo.DialogTests.withRecyclerView;
import static com.ad340.whatdo.MaterialDatePickerTestUtils.clickCancel;
import static com.ad340.whatdo.MaterialDatePickerTestUtils.clickDayDatePicker;
import static com.ad340.whatdo.MaterialDatePickerTestUtils.clickDayDateRangePicker;
import static com.ad340.whatdo.MaterialDatePickerTestUtils.clickOk;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

// 7 tests
@RunWith(AndroidJUnit4.class)
public class ViewByTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /*
    Tests that the "viewing" bar loads
    */
    @Test
    public void viewingBarLoads() {
        onView(withId(R.id.viewing_date_card))
                .check(matches(isDisplayed()));
    }

    /*
    Tests that the "viewing by" dialog opens and closes with x button
     */
    @Test
    public void viewByMenuOpensCloses() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        pressBack();
        onView(withId(R.id.top_app_bar)).check(matches(isDisplayed()));
    }

    /*
    Tests that DatePickerDialog opens & closes
     */
    @Test
    public void datePickerDialogsOpenClose() {
        closeSoftKeyboard();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Single Day"))
            .perform(click());
        clickCancel();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Date Range"))
            .perform(click());
        clickCancel();
        onView(withId(R.id.top_app_bar))
            .check(matches(isDisplayed()));
    }

    /*
    Tests that the "viewing" bar updates
    */
    @Test
    public void viewingBarUpdatesDate() throws InterruptedException {
        onView(withId(R.id.viewing_date_text)).check(matches(withText(R.string.all_upcoming)));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(500);
        int testDay = 28;
        onView(withText("Single Day")).perform(click());
        Thread.sleep(3000);
        clickDayDatePicker(testDay);
        Thread.sleep(500);
        clickOk();
        onView(withId(R.id.viewing_date_text)).check(matches(withText("June 28, 2020")));

        // go back to all upcoming (for other tests)
        resetDateRange();
    }

    /*
Tests that the "viewing" bar updates
*/
    @Test
    public void viewingBarUpdatesDateRange() throws InterruptedException {
        closeSoftKeyboard();
        Thread.sleep(500);
        onView(withId(R.id.viewing_date_text)).check(matches(withText(R.string.all_upcoming)));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(500);
        int testDay = 12;
        onView(withText("Date Range")).perform(click());
        Thread.sleep(3000);
        clickDayDateRangePicker(testDay, "June, 2020");
        Thread.sleep(1500);
        clickDayDateRangePicker(testDay, "July, 2020");
        clickOk();
        onView(withId(R.id.viewing_date_text))
                .check(matches(withText("June 12, 2020 to July 12, 2020")));
        // go back to all upcoming (for other tests)
        resetDateRange();
    }

    /*
    Test add a task on a date, then view only that date, and that task appears
     */
    @Test
    public void newTodosFilter() throws InterruptedException {
        int year = 2020;
        int month = 6;
        int dayOfMonth = 28;

        closeSoftKeyboard();
        Thread.sleep(500);
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.create_todo_task_name_edit_text))
                .perform(typeText("Test Task"), closeSoftKeyboard());

        Thread.sleep(500);
        onView(withId(R.id.create_todo_date_btn)).perform(click());
        onView(withClassName(equalTo(
                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.create_todo_finish_btn)).perform(click());

        // this will break in July - figure out how to change month
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        Thread.sleep(500);
        onView(withText("Single Day"))
                .perform(click());
        Thread.sleep(3000);
        clickDayDatePicker(dayOfMonth);
        clickOk();

        Thread.sleep(500);
        onView(allOf(
                withText("Test Task"),
                withResourceName("name_text"),
                isDisplayed()
                ))
                .check(matches(withText("Test Task")));
        // go back to all upcoming (for other tests)
        resetDateRange();
    }

    /*
        Tests that a tag filter can be applied to new todos and todos in the recyclerview
    */
    @Test
    public void tagFilter() throws InterruptedException {

        // add new todo
        closeSoftKeyboard();
        Thread.sleep(500);
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.create_todo_task_name_edit_text))
                .perform(typeText("Test Task2"), closeSoftKeyboard());

        Thread.sleep(500);
        onView(withId(R.id.create_todo_tag_btn)).perform(click());
        Thread.sleep(500);
        onView(withText("Add New Tag")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.add_tag_edit_text)).perform(typeText("test tag2"), closeSoftKeyboard());
        onView(withId(R.id.add_tag_finish_btn)).perform(click());
        onView(withId(R.id.create_todo_finish_btn)).perform(click());

        // add tag to an existing todo
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());
        closeSoftKeyboard();
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.tag_btn))
                .perform(click());
        onView(withText("test tag2"))
                .perform(click());
        // this will break in July - figure out how to change month
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        Thread.sleep(500);
        onView(withText("Tag Filter")).perform(click());
        onView(withText("test tag2")).perform(click());

        Thread.sleep(500);
        onView(allOf(withText("Test Task2"), withResourceName("name_text"), isDisplayed()))
                .check(matches(withText("Test Task2")));
        onView(withText("First Todo")).check(matches(isDisplayed()));
    }

    public static void resetDateRange() throws InterruptedException {
        // go back to all upcoming (for other tests)
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(500);
        onView(withText("All Upcoming"))
                .perform(click());
    }
}
