package com.ad340.whatdo;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Month;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class ViewByTest {

    static final Object MONTHS_VIEW_GROUP_TAG = "MONTHS_VIEW_GROUP_TAG";
    static final Object CONFIRM_BUTTON_TAG = "CONFIRM_BUTTON_TAG";

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
    Tests that the "viewing" bar updates
    */
    @Test
    public void viewingBarUpdates() throws InterruptedException {
        onView(withId(R.id.viewing_date_text))
                .check(matches(withText(R.string.all_upcoming)));
        onView(withId(R.id.top_app_bar))
                .perform(click());
        Thread.sleep(500);
        int testRangeMonth = 6;
        Month testRangeMo = Month.JUNE;
        int testRangeDay = 1;
        onView(withId(R.id.view_by_date))
                .perform(click());
        Thread.sleep(3000);
//        onView(withClassName(equalTo(DatePicker.class.getName())))
//                .perform(setDate(testRangeYear, testRangeMonth, testRangeDay));
//        onView(withId(android.R.id.button1)).perform(click());
        clickDay(testRangeMo, testRangeDay);
        clickOk();
        onView(withId(R.id.close_view_by_dialog)).perform(click());
        onView(withId(R.id.viewing_date_text))
                .check(matches(withText("June 01, 2020")));
    }

    // HELPERS FOR MATERIAL DATE PICKERS
    public static void clickDay(Month month, int day) {
        onView(
                allOf(
                        isDescendantOfA(withTagValue(equalTo(MONTHS_VIEW_GROUP_TAG))),
                        withTagValue(IsEqual.<Object>equalTo(month)),
                        withText(String.valueOf(day))))
                .perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    static void clickOk() {
        onView(withTagValue(equalTo(CONFIRM_BUTTON_TAG))).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }
}
