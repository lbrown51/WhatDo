package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.DatePicker;
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

    public static DatePickerDialog.OnDateSetListener onDateSetListener ( // RecyclerViewAdapter
            Todo todo, ToDoItemRecyclerViewAdapter.ToDoItemViewHolder holder,
            OnTodoInteractionListener listener) {
        return (view, year, monthOfYear, dayOfMonth) -> {
            cDate.set(Calendar.YEAR, year);
            cDate.set(Calendar.MONTH, monthOfYear);
            cDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            StringBuilder dateString = new StringBuilder(DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));
            Log.i(TAG, "onDateSetListener: original");
            Log.i(TAG, DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));
            holder.toDoDate.setText(dateString);
            listener.onUpdateTodo(todo, String.valueOf(dateString), Constants.DATE);
            resetDate();
        };
    }

    public static DatePickerDialog.OnDateSetListener onDateSetListener (  // MainActivity
            StringBuilder dateString, TextView v) {
        return (view, year, monthOfYear, dayOfMonth) -> {
            cDate.set(Calendar.YEAR, year);
            cDate.set(Calendar.MONTH, monthOfYear);
            cDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            dateString.setLength(0);
            dateString.append(DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));
            Log.i(TAG, "onDateSetListener: original");
            Log.i(TAG, DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));
            v.setText(dateString);
            resetDate();
        };
    }



    public static void setDatePicker(Context context, ImageButton imageButton, DatePickerDialog.OnDateSetListener date) {
        imageButton.setOnClickListener(view -> {
                    DatePickerDialog dialog = new DatePickerDialog(context, date,
                            cDate.get(Calendar.YEAR),
                            cDate.get(Calendar.MONTH),
                            cDate.get(Calendar.DAY_OF_MONTH));
                    dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                            "Rec", (dialogInterface, i) -> {
                                Log.i(TAG, "setDatePickerShowOnClick: ");
                                Log.i(TAG, String.valueOf(dialog.getDatePicker().getDayOfMonth()));
                                Log.i(TAG, DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));


                                RecurringTodoFragment recurringTodoFragment =
                                        new RecurringTodoFragment(date, imageButton, cDate);
                                recurringTodoFragment.show(((MainActivity) context)
                                        .getSupportFragmentManager(), "recurringFrag");
                            });
                    dialog.show();
        });
    }

    public static void setDatePicker(Context context, DatePickerDialog.OnDateSetListener date) {
        DatePickerDialog dialog = new DatePickerDialog(context, date,
                cDate.get(Calendar.YEAR),
                cDate.get(Calendar.MONTH),
                cDate.get(Calendar.DAY_OF_MONTH));
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                "Rec", (dialogInterface, i) -> {
                    Log.i(TAG, "setDatePickerShowOnClick: ");
                    Log.i(TAG, String.valueOf(dialog.getDatePicker().getDayOfMonth()));
                    Log.i(TAG, DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));


                    RecurringTodoFragment recurringTodoFragment =
                            new RecurringTodoFragment(date, cDate);
                    recurringTodoFragment.show(((MainActivity) context)
                            .getSupportFragmentManager(), "recurringFrag");
                });
        dialog.show();
    }

    public static void showDatePicker(Context context, ImageButton imageButton,
                                      DatePickerDialog.OnDateSetListener date) { // Recurring Dialog

        DatePickerDialog dialog = new DatePickerDialog(context, date,
                cDate.get(Calendar.YEAR),
                cDate.get(Calendar.MONTH),
                cDate.get(Calendar.DAY_OF_MONTH));

        DatePicker.OnDateChangedListener onDateChangedListener =
                (datePicker, year, month, dayOfMonth) -> {
            Log.i(TAG, "onDateChanged: ");
            Log.i(TAG, DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));
            datePicker.updateDate(year, month, dayOfMonth);
        };

        DatePickerDialog.OnDateSetListener onDateSetListener =
                (datePicker, year, month, dayOfMonth) -> {
            Log.i(TAG, "onDateSet: ");
            Log.i(TAG, DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));
            datePicker.updateDate(cDate.get(Calendar.YEAR), cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH));
        };

        dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                "Rec", (dialogInterface, i) -> {
                    Log.i(TAG, "showDatePicker: ");
                    Log.i(TAG, DateFormat.getDateInstance(DateFormat.FULL).format(cDate.getTime()));
                    RecurringTodoFragment recurringTodoFragment =
                            new RecurringTodoFragment(date, imageButton, cDate);
                    recurringTodoFragment.show(((MainActivity) context)
                            .getSupportFragmentManager(), "recurringFrag");
                });
        onDateChangedListener.onDateChanged(dialog.getDatePicker(),
                dialog.getDatePicker().getYear(), dialog.getDatePicker().getMonth(),
                dialog.getDatePicker().getDayOfMonth());
        onDateSetListener.onDateSet(dialog.getDatePicker(), dialog.getDatePicker().getYear(),
                dialog.getDatePicker().getMonth(), dialog.getDatePicker().getDayOfMonth());

        dialog.show();
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

    // private helpers
    private static void resetDate() {
        Calendar cTodayDate = Calendar.getInstance();
        cDate.set(Calendar.YEAR, cTodayDate.get(Calendar.YEAR));
        cDate.set(Calendar.MONTH, cTodayDate.get(Calendar.MONTH));
        cDate.set(Calendar.DAY_OF_MONTH, cTodayDate.get(Calendar.DAY_OF_MONTH));
    }

    private static void resetTime() {
        Calendar cTodayTime = Calendar.getInstance();
        cTime.set(Calendar.HOUR_OF_DAY, cTodayTime.get(Calendar.HOUR_OF_DAY));
        cTime.set(Calendar.MINUTE, cTodayTime.get(Calendar.MINUTE));
    }
}