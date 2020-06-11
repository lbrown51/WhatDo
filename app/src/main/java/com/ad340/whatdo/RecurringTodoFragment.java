package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

import static com.ad340.whatdo.PickerUtils.resetDate;
import static com.ad340.whatdo.PickerUtils.setDatePicker;

public class RecurringTodoFragment extends DialogFragment {
    private static final String TAG = RecurringTodoFragment.class.getName();
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Calendar cRecurring;

    public RecurringTodoFragment(DatePickerDialog.OnDateSetListener onDateSetListener, Calendar c) {
        this.onDateSetListener = onDateSetListener;
        this.cRecurring = c;
        Log.i(TAG, "RecurringTodoFragment: ");
        Log.i(TAG, String.valueOf(cRecurring.get(Calendar.DAY_OF_MONTH)));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.todo_recurring_dialog, null))
        .setTitle(R.string.recurring_tast_title)
        .setPositiveButton(R.string.confirm_recurring, (dialogInterface, i) -> {
            setDatePicker(getContext(), onDateSetListener);
        })
        .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
            resetDate();
            dismiss();
        });

        return builder.create();
    }
}
