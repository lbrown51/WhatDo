package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.ImageButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PickerUtils {

    public static DatePickerDialog.OnDateSetListener onDateSetListener ( // RecyclerViewAdapter
            Calendar c, Todo todo, ToDoItemRecyclerViewAdapter.ToDoItemViewHolder holder,
            OnTodoInteractionListener listener) {
        return (view, year, monthOfYear, dayOfMonth) -> {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            StringBuilder dateString = new StringBuilder(DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()));

            holder.toDoDate.setText(dateString);
            listener.onUpdateTodo(todo, String.valueOf(dateString), Constants.DATE);

        };
    }

    public static DatePickerDialog.OnDateSetListener onDateSetListener (  // MainActivity
            Calendar c, StringBuilder dateString) {
        return (view, year, monthOfYear, dayOfMonth) -> {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            dateString.append(DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()));
        };
    }

    public static void showDatePicker(Context context, Calendar c, ImageButton imageButton,
                                      DatePickerDialog.OnDateSetListener date) {
        imageButton.setOnClickListener(view ->
                new DatePickerDialog(context, date,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show()
        );
    }


    public static TimePickerDialog.OnTimeSetListener onTimeSetListener ( // RecyclerViewAdapter
            Calendar c, Todo todo, ToDoItemRecyclerViewAdapter.ToDoItemViewHolder holder,
            OnTodoInteractionListener listener) {
        return (view, hourOfDay, minute) -> {
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
            String timeString = sdf.format(c.getTime());

            holder.toDoTime.setText(timeString);
            listener.onUpdateTodo(todo, timeString, Constants.TIME);

        };
    }

    public static TimePickerDialog.OnTimeSetListener onTimeSetListener ( // MainActivity
            Calendar c, StringBuilder timeString) {
        return (view, hourOfDay, minute) -> {
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
            timeString.append(sdf.format(c.getTime()));
        };
    }


    public static void showTimePicker(Context context, Calendar c, ImageButton imageButton,
                                      TimePickerDialog.OnTimeSetListener time) {
        imageButton.setOnClickListener(view ->
                new TimePickerDialog(context, time,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE), false).show());
    }





}