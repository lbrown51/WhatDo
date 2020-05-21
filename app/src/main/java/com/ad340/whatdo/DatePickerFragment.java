package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment {
    private StringBuilder currentDateString;
    private Todo todo;

    public DatePickerFragment(Todo todo) {
        this.todo = todo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(Objects.requireNonNull(getActivity()),
                (DatePickerDialog.OnDateSetListener) this, year, month, day);
    }

//    @Override
//    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//        Calendar c = Calendar.getInstance();
//
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, month);
//        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//        currentDateString = new StringBuilder();
//
//        currentDateString.append(DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()));
//        this.todo.setDate(String.valueOf(currentDateString));
//    }
}
