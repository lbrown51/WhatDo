package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PickerUtils {

    public static DatePickerDialog.OnDateSetListener onDateSetListener (
            Calendar c, Todo todo, ToDoItemRecyclerViewAdapter.ToDoItemViewHolder holder,
            OnTodoInteractionListener listener) {
        return (view, year, monthOfYear, dayOfMonth) -> {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            StringBuilder dateString = new StringBuilder(DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()));

            holder.toDoDate.setText(dateString);
            if (listener != null) {
                listener.onUpdateTodo(todo, String.valueOf(dateString), Constants.DATE);
            }
        };
    }

    public static TimePickerDialog.OnTimeSetListener onTimeSetListener (
            Calendar c, Todo todo, ToDoItemRecyclerViewAdapter.ToDoItemViewHolder holder,
            OnTodoInteractionListener listener) {
        return (view, hourOfDay, minute) -> {
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
            String timeString = sdf.format(c.getTime());

            holder.toDoTime.setText(timeString);
            if (listener != null) {
                listener.onUpdateTodo(todo, timeString, Constants.TIME);
            }
        };
    }
}