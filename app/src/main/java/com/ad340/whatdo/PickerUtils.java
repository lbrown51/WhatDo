package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PickerUtils {
    private static final String TAG = PickerUtils.class.getName();
    private static final Calendar cDate = Calendar.getInstance();
    private static final Calendar cTime = Calendar.getInstance();

    private static Integer rInterval;
    private static String rDays;

    public static DatePickerDialog.OnDateSetListener onDateSetListener ( // RecyclerViewAdapter
            Todo todo, ToDoItemRecyclerViewAdapter.ToDoItemViewHolder holder,
            OnTodoInteractionListener listener) {
        return (view, year, monthOfYear, dayOfMonth) -> {
            setUserDate(year, monthOfYear, dayOfMonth);

            Log.i(TAG, "onDateSetListener: RecyclerViewAdapter");
            if (rInterval != null) {
                Log.i(TAG, String.valueOf(rInterval));
                Log.i(TAG, rDays);
            }


            StringBuilder dateString = new StringBuilder(
                    DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));
            holder.toDoDate.setText(dateString);
            listener.onUpdateTodo(todo, String.valueOf(dateString), Constants.DATE);
            resetDate();
        };
    }

    public static DatePickerDialog.OnDateSetListener onDateSetListener (  // MainActivity
            StringBuilder dateString, TextView v) {
        return (view, year, monthOfYear, dayOfMonth) -> {
            setUserDate(year, monthOfYear, dayOfMonth);

            Log.i(TAG, "onDateSetListener: CreateDialog");
            if (rInterval != null) {
                Log.i(TAG, String.valueOf(rInterval));
                Log.i(TAG, rDays);
            }

            dateString.setLength(0);
            dateString.append(DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));
            v.setText(dateString);
            resetDate();
        };
    }

    public static void setDatePicker(
            Context context, ImageButton imageButton, DatePickerDialog.OnDateSetListener date) {
        imageButton.setOnClickListener(view -> {
            initDatePicker(context, date);
        });
    }

    public static void setDatePicker(Context context, DatePickerDialog.OnDateSetListener date) {
        initDatePicker(context, date);
    }

    public static void setDatePicker(Context context, DatePickerDialog.OnDateSetListener date,
                                     int recurrenceInterval, String recurrenceDays) {
        rInterval = recurrenceInterval;
        rDays = recurrenceDays;
        initDatePicker(context, date);
    }

    public static TimePickerDialog.OnTimeSetListener onTimeSetListener ( // RecyclerViewAdapter
            Todo todo, ToDoItemRecyclerViewAdapter.ToDoItemViewHolder holder,
            OnTodoInteractionListener listener) {
        return (view, hourOfDay, minute) -> {
            cTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cTime.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
            String timeString = sdf.format(cTime.getTime());

            holder.toDoTime.setText(timeString);
            listener.onUpdateTodo(todo, timeString, Constants.TIME);
            resetTime();
        };
    }

    public static TimePickerDialog.OnTimeSetListener onTimeSetListener ( // MainActivity
            StringBuilder timeString, TextView v) {
        return (view, hourOfDay, minute) -> {
            cTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cTime.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
            timeString.setLength(0);
            timeString.append(sdf.format(cTime.getTime()));
            v.setText(timeString);
            resetTime();
        };
    }


    public static void setTimePickerShowOnClick(Context context, ImageButton imageButton,
                                                TimePickerDialog.OnTimeSetListener time) {
        imageButton.setOnClickListener(view ->
                new TimePickerDialog(context, time,
                        cTime.get(Calendar.HOUR_OF_DAY),
                        cTime.get(Calendar.MINUTE), false).show());
    }

    // helpers
    public static void resetDate() {
        Calendar cTodayDate = Calendar.getInstance();
        cDate.set(Calendar.YEAR, cTodayDate.get(Calendar.YEAR));
        cDate.set(Calendar.MONTH, cTodayDate.get(Calendar.MONTH));
        cDate.set(Calendar.DAY_OF_MONTH, cTodayDate.get(Calendar.DAY_OF_MONTH));
    }

    private static void setUserDate(int year, int month, int dayOfMonth) {
        cDate.set(Calendar.YEAR, year);
        cDate.set(Calendar.MONTH, month);
        cDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private static void resetTime() {
        Calendar cTodayTime = Calendar.getInstance();
        cTime.set(Calendar.HOUR_OF_DAY, cTodayTime.get(Calendar.HOUR_OF_DAY));
        cTime.set(Calendar.MINUTE, cTodayTime.get(Calendar.MINUTE));
    }

    private static void initDatePicker(Context context, DatePickerDialog.OnDateSetListener date) {
        DatePickerDialog dialog = new DatePickerDialog(
                context, date, cDate.get(Calendar.YEAR), cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH));
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                context.getString(R.string.set_recurring), (dialogInterface, i) -> {
                    cDate.set(Calendar.YEAR, dialog.getDatePicker().getYear());
                    cDate.set(Calendar.MONTH, dialog.getDatePicker().getMonth());
                    cDate.set(Calendar.DAY_OF_MONTH, dialog.getDatePicker().getDayOfMonth());

                    RecurringTodoFragment recurringTodoFragment =
                            new RecurringTodoFragment(date, cDate);
                    recurringTodoFragment.show(((MainActivity) context)
                            .getSupportFragmentManager(), Constants.TAG_RECURRING_FRAG);
                });
        dialog.show();
    }
}