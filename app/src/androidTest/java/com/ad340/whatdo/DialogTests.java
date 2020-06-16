package com.ad340.whatdo;

import android.view.KeyEvent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.contrib.PickerActions.setTime;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class DialogTests {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);


    public static RecyclerViewMatcherTestUtils withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcherTestUtils(recyclerViewId);
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
            // TODO Add more add todo checks
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
            // TODO Add more add todo checks
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.fab))
                    .check(matches(isDisplayed()));
        }
    }

    /*
        Tests whether the create to-do dialog closes with back button
    */
    @Test
    public void openCloseDialogWithBackKey() throws InterruptedException {
        Thread.sleep(250);

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.create_todo_dialog))
                .check(matches(isDisplayed()));
        pressKey(KeyEvent.KEYCODE_0);
        Espresso.pressBack();
        try {
            onView(withId(R.id.create_todo_dialog))
                    .check(matches(not(isDisplayed())));
            // TODO Add more add todo checks
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.fab))
                    .check(matches(isDisplayed()));
        }
    }

    /*
       Tests whether create new task form has correct fields
    */
    @Test
    public void createNewTodoHasCorrectFields() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.create_todo_dialog))
                .check(matches(isDisplayed()));

        onView(withId(R.id.create_todo_task_name_layout))
                .check(matches(isDisplayed()));
        onView(withId(R.id.create_todo_finish_btn))
                .check(matches(isDisplayed()));
        onView(withId(R.id.create_todo_title))
                .check(matches(isDisplayed()));
        onView(withId(R.id.create_todo_time_btn))
                .check(matches(isDisplayed()));
        onView(withId(R.id.create_todo_date_btn))
                .check(matches(isDisplayed()));
        onView(withId(R.id.create_todo_notes_btn))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(withId(R.id.create_todo_notes_text))
                .check(matches(isDisplayed()));
    }

    /*
       Tests if empty tasks won't be created.
    */
    @Test
    public void emptyTaskStopsTodoCreate() throws InterruptedException {
        Thread.sleep(1000);
        closeSoftKeyboard();
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.create_todo_dialog))
                .check(matches(isDisplayed()));

        onView(withId(R.id.create_todo_finish_btn))
                .perform(click());
        onView(withId(R.id.create_todo_dialog))
                .check(matches(isDisplayed()));
    }

    /*
       Tests if new tasks can be created.
    */
    @Test
    public void canCreateNewTodo() throws InterruptedException {
        int hourOfDay = 16;
        int minute = 37;
        int year = 2020;
        int month = 6;
        int dayOfMonth = 28;

        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.create_todo_dialog))
                .check(matches(isDisplayed()));

        onView(withId(R.id.create_todo_task_name_edit_text))
                .perform(typeText("New Task3"), closeSoftKeyboard());

        onView(withId(R.id.create_todo_time_btn)).perform(click());
        Thread.sleep(500);
        onView(withClassName(Matchers.equalTo(
                TimePicker.class.getName()))).perform(setTime(hourOfDay, minute));
        onView(withId(android.R.id.button1)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.create_todo_time_text))
                .check(matches(withText("4:37 PM")));

        Thread.sleep(500);
        onView(withId(R.id.create_todo_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(
                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.create_todo_date_text))
                .check(matches(withText("6/28/20")));

        Thread.sleep(500);
        onView(withId(R.id.create_todo_notes_btn))
                .perform(click());

        Thread.sleep(500);
        onView(withId(R.id.create_todo_notes_text))
                .perform(typeText("About my new task"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.create_todo_finish_btn))
                .perform(click());

        Thread.sleep(500);
        onView(withId(R.id.todo_list_recycler_view))
                .check(matches(isDisplayed()));

        onView(withText("New Task3")).check(matches(withText("New Task3")));

        onView(withText("New Task3")).perform(click());

        Thread.sleep(500);
        onView(withText("4:37 PM")).check(matches(isDisplayed()));

        onView(withText("Sunday, June 28, 2020"))
                .check(matches(isDisplayed()));

        onView(withText("About my new task"))
                .check(matches(isDisplayed()));

        onView(withId(R.id.fab))
                .check(matches(isDisplayed()));

    }

    /*
    Tests if the recurring dialog opens correctly
*/
    @Test
    public void canOpenRecurringDialog() throws InterruptedException {
        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.create_todo_date_btn)).perform(click());
        Thread.sleep(500);
        onView(withId(android.R.id.button3)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.recurring_container)).check(matches(isDisplayed()));
        onView(withId(R.id.r_title)).check(matches(isDisplayed()));
        onView(withId(R.id.r_chipgroup_type)).check(matches(isDisplayed()));
        onView(withId(R.id.r_interval)).check(matches(isDisplayed()));
        onView(withId(R.id.r_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.r_confirm_button)).check(matches(isDisplayed()));
    }

    /*
        Tests if the recurring dialog toggles correctly
    */
    @Test
    public void recurringDialogToggles() throws InterruptedException {
        final DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        Thread.sleep(500);
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.create_todo_date_btn)).perform(click());
        onView(withId(android.R.id.button3)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.chip_weekly)).perform(click());
        onView(withId(R.id.r_chipgroup_days)).check(matches(isDisplayed()));
        onView(withText(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)))
                .check(matches(isChecked()));
        onView(withId(R.id.r_chipgroup_days))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.chip_daily)).perform(click());
        onView(withId(R.id.r_chipgroup_days))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

    }

    /*
    Tests if the recurring dialog cancels correctly
    */
    @Test
    public void recurringDialogCancels() throws InterruptedException {
        int year = 2020;
        int month = 7;
        int dayOfMonth = 25;
        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.create_todo_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(
                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button3)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.r_cancel_button)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("7/25/20"))
                .check(matches(isDisplayed()));
    }

    /*
    Tests if the recurring dialog confirms a daily recurrence correctly
    */
    @Test
    public void recurringDialogDailySubmit() throws InterruptedException {
        int year = 2020;
        int month = 8;
        int dayOfMonth = 14;
        closeSoftKeyboard();
        onView(withId(R.id.fab)).perform(click());
        closeSoftKeyboard();
        Thread.sleep(500);
        onView(withId(R.id.create_todo_is_recurring)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.create_todo_date_btn)).perform(click());
        closeSoftKeyboard();
        onView(withClassName(Matchers.equalTo(
                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button3)).perform(click());
        closeSoftKeyboard();
        Thread.sleep(500);
        onView(withId(R.id.r_interval)).perform(typeText("30"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.r_confirm_button)).perform(click());
        closeSoftKeyboard();
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("8/14/20"))
                .check(matches(isDisplayed()));
        onView(withId(R.id.create_todo_is_recurring)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        closeSoftKeyboard();
    }

    /*
   Tests if the recurring dialog confirms a weekly recurrence correctly
   */
    @Test
    public void recurringDialogWeeklySubmit() throws InterruptedException {
        int year = 2020;
        int month = 12;
        int dayOfMonth = 13;
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.create_todo_date_btn)).perform(click());
        Thread.sleep(500);
        onView(withClassName(Matchers.equalTo(
                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button3)).perform(click());
        closeSoftKeyboard();
        Thread.sleep(500);
        onView(withId(R.id.chip_weekly)).perform(click());
        onView(withId(R.id.r_interval)).perform(typeText("4"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.r_confirm_button)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("12/13/20"))
                .check(matches(isDisplayed()));
    }

    /*
        Tests if the recurring dialog input validation works
    */
    @Test
    public void recurringDialogValidatesInput() throws InterruptedException {
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.create_todo_date_btn)).perform(click());
        onView(withId(android.R.id.button3)).perform(click());
        Thread.sleep(500);

        onView(withId(R.id.r_confirm_button)).perform(click());
        onView(withId(R.id.recurring_container)).check(matches(isDisplayed()));
        onView(withId(R.id.r_interval)).perform(typeText("0"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.r_interval)).check(matches(withText("")));
        onView(withId(R.id.r_interval)).perform(typeText("-3"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.r_interval)).check(matches(withText("3")));
    }

    /*
        Tests if the recurring dialog confirms a weekly recurrence correctly
    */
    @Test
    public void recurringDialogWeeklySubmitAndTodoSubmit() throws InterruptedException {
        int year = 2020;
        int month = 12;
        int dayOfMonth = 13;
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.create_todo_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(
                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button3)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.chip_weekly)).perform(click());
        onView(withId(R.id.r_interval)).perform(typeText("4"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.r_confirm_button)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("12/13/20"))
                .check(matches(isDisplayed()));


        onView(withId(R.id.create_todo_task_name_edit_text))
                .perform(typeText("New Task"), closeSoftKeyboard());

        onView(withId(R.id.create_todo_finish_btn))
                .perform(click());
        onView(withId(R.id.todo_list_recycler_view))
                .check(matches(isDisplayed()));
        onView(withText("New Task"))
                .check(matches(withText("New Task")));
        onView(withText("New Task"))
                .perform(click());

    }

//    @Test
//    public void recurringDialogSetsIcon() throws InterruptedException {
//        int year = 2020;
//        int month = 12;
//        int dayOfMonth = 13;
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.name_text))
//                .perform(click());
//        closeSoftKeyboard();
//
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.is_recurring))
//                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
//
//        Thread.sleep(500);
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.date_btn))
//                .perform(click());
//        onView(withClassName(Matchers.equalTo(
//                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
//        onView(withId(android.R.id.button3)).perform(click());
//
//        Thread.sleep(500);
//        onView(withId(R.id.r_interval)).perform(typeText("30"));
//        onView(withId(R.id.r_confirm_button)).perform(click());
//        onView(withId(android.R.id.button1)).perform(click());
//
//        onView(withRecyclerView(R.id.todo_list_recycler_view)
//                .atPositionOnView(0, R.id.is_recurring))
//                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
//    }

    @Test
    public void recurringCheckRecurrenceDaily() throws InterruptedException {
        int year = 2020;
        int month = 12;
        int dayOfMonth = 13;
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());
        closeSoftKeyboard();

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.is_recurring))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        Thread.sleep(500);
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_btn))
                .perform(click());
        onView(withClassName(Matchers.equalTo(
                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button3)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.r_interval)).perform(typeText("30"));
        onView(withId(R.id.r_confirm_button)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.todo_item_finished_checkbox))
                .perform(click());

        onView(withText("First Todo"))
                .check(matches(isDisplayed()));

        onView(withText("Tuesday, January 12, 2021"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void recurringCheckRecurrenceWeekly() throws InterruptedException {
        int year = 2020;
        int month = 12;
        int dayOfMonth = 13;
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.name_text))
                .perform(click());
        closeSoftKeyboard();

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.is_recurring))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        Thread.sleep(500);
        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.date_btn))
                .perform(click());
        onView(withClassName(Matchers.equalTo(
                DatePicker.class.getName()))).perform(setDate(year, month, dayOfMonth));
        onView(withId(android.R.id.button3)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.chip_weekly)).perform(click());
        onView(withId(R.id.chip_tuesday)).perform(click());
        onView(withId(R.id.chip_thursday)).perform(click());
        onView(withId(R.id.r_interval)).perform(typeText("1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.r_confirm_button)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.todo_item_finished_checkbox))
                .perform(click());

        onView(withText("First Todo"))
                .check(matches(isDisplayed()));

        onView(withText("Tuesday, December 15, 2020"))
                .check(matches(isDisplayed()));

        onView(withRecyclerView(R.id.todo_list_recycler_view)
                .atPositionOnView(0, R.id.todo_item_finished_checkbox))
                .perform(click());

        onView(withText("First Todo"))
                .check(matches(isDisplayed()));
        onView(withText("Thursday, December 17, 2020"))
                .check(matches(isDisplayed()));
    }
}
