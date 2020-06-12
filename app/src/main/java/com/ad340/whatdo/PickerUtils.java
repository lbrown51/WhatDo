package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PickerUtils {
    private static final String TAG = PickerUtils.class.getName();
    private static final Calendar c = Calendar.getInstance();

    private static String rEncoded = Constants.NO_RECURRENCE;

    public static DatePickerDialog.OnDateSetListener onDateSetListener ( // RecyclerViewAdapter
            Todo todo, ToDoItemRecyclerViewAdapter.ToDoItemViewHolder holder,
            OnTodoInteractionListener listener) {
        return (view, year, monthOfYear, dayOfMonth) -> {
            setUserDate(year, monthOfYear, dayOfMonth);

            Log.i(TAG, "onDateSetListener: RecyclerViewAdapter");
            Log.i(TAG, rEncoded);

            StringBuilder dateString = new StringBuilder(
                    DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime()));
            holder.toDoDate.setText(dateString);
            try {
                listener.onUpdateTodo(todo, String.valueOf(dateString), Constants.DATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            resetDate();
        };
    }

    public static DatePickerDialog.OnDateSetListener onDateSetListener (  // MainActivity
            StringBuilder dateString, TextView v) {
        return (view, year, monthOfYear, dayOfMonth) -> {
            setUserDate(year, monthOfYear, dayOfMonth);

            Log.i(TAG, "onDateSetListener: CreateDialog");
            Log.i(TAG, String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

            dateString.setLength(0);
            // I changed this to DateFormat.SHORT format so I can parse it more easily
            dateString.append(DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime()));
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

    public static void setDatePicker(Context context, DatePickerDialog.OnDateSetListener date, String recurrenceEncoded) {
        rEncoded = recurrenceEncoded;
        initDatePicker(context, date);
    }

    public static TimePickerDialog.OnTimeSetListener onTimeSetListener ( // RecyclerViewAdapter
            Todo todo, ToDoItemRecyclerViewAdapter.ToDoItemViewHolder holder,
            OnTodoInteractionListener listener) {
        return (view, hourOfDay, minute) -> {
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
            String timeString = sdf.format(c.getTime());

            holder.toDoTime.setText(timeString);
            try {
                listener.onUpdateTodo(todo, timeString, Constants.TIME);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            resetTime();


        };
    }

    public static TimePickerDialog.OnTimeSetListener onTimeSetListener ( // MainActivity
            StringBuilder timeString, TextView v) {
        return (view, hourOfDay, minute) -> {
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
            timeString.setLength(0);
            timeString.append(sdf.format(c.getTime()));
            v.setText(timeString);
            resetTime();
        };
    }


    public static void setTimePickerShowOnClick(Context context, ImageButton imageButton,
                                                TimePickerDialog.OnTimeSetListener time) {
        imageButton.setOnClickListener(view ->
                new TimePickerDialog(context, time,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE), false).show());
    }

    // helpers
    public static void resetDate() {
        Calendar cTodayDate = Calendar.getInstance();
        c.set(Calendar.YEAR, cTodayDate.get(Calendar.YEAR));
        c.set(Calendar.MONTH, cTodayDate.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_MONTH, cTodayDate.get(Calendar.DAY_OF_MONTH));
    }

    private static void setUserDate(int year, int month, int dayOfMonth) {
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private static void resetTime() {
        Calendar cTodayTime = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, cTodayTime.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, cTodayTime.get(Calendar.MINUTE));
    }

    private static void initDatePicker(Context context, DatePickerDialog.OnDateSetListener date) {
        DatePickerDialog dialog = new DatePickerDialog(
                context, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, context.getString(R.string.set_recurring),
            (dialogInterface, i) -> {
                c.set(Calendar.YEAR, dialog.getDatePicker().getYear());
                c.set(Calendar.MONTH, dialog.getDatePicker().getMonth());
                c.set(Calendar.DAY_OF_MONTH, dialog.getDatePicker().getDayOfMonth());

                RecurringTodoFragment recurringTodoFragment =
                        new RecurringTodoFragment(date);
                recurringTodoFragment.show(((MainActivity) context)
                        .getSupportFragmentManager(), Constants.TAG_RECURRING_FRAG);
                });
        dialog.show();
    }

    public static Calendar getCalendar() {
        return c;
    }
}
