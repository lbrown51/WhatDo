package com.ad340.whatdo;

/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsEqual.equalTo;

import android.app.Activity;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.util.Pair;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.test.espresso.ViewInteraction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Month;
import java.util.Calendar;
import org.hamcrest.core.IsEqual;

public final class MaterialDatePickerTestUtils {

    static final Object MONTHS_VIEW_GROUP_TAG = "MONTHS_VIEW_GROUP_TAG";
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