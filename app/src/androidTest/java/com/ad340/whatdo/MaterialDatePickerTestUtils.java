package com.ad340.whatdo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsEqual.equalTo;
import androidx.test.platform.app.InstrumentationRegistry;

public final class MaterialDatePickerTestUtils {

    static final Object CONFIRM_BUTTON_TAG = "CONFIRM_BUTTON_TAG";
    static final Object CANCEL_BUTTON_TAG = "CANCEL_BUTTON_TAG";
    static final String dateRangeParentName = "month_grid";
    static final String dateParentName = "mtrl_calendar_months";

    private MaterialDatePickerTestUtils() {}

    public static void clickDayDatePicker(int day) {
        onView(
                allOf(
                        isDescendantOfA(withResourceName(dateParentName)),
                        withText(String.valueOf(day)),
                        isDisplayed()
                ))
                .perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    public static void clickDayDateRangePicker(int day, String monthString) {
        onView(
                allOf(
                        isDescendantOfA(withResourceName(dateRangeParentName)),
                        isDescendantOfA(hasSibling(withText(monthString))),
                        withText(String.valueOf(day)),
                        isDisplayed()
                ))
                .perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    static void clickOk() {
        onView(withTagValue(equalTo(CONFIRM_BUTTON_TAG))).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    static void clickCancel() {
        onView(withTagValue(equalTo(CANCEL_BUTTON_TAG))).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

}