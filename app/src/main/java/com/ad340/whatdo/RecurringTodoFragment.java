package com.ad340.whatdo;


import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;
import java.util.Objects;

import static com.ad340.whatdo.PickerUtils.resetDate;
import static com.ad340.whatdo.PickerUtils.setDatePicker;

public class RecurringTodoFragment extends DialogFragment {
    private static final String TAG = RecurringTodoFragment.class.getName();
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Calendar cRecurring;

    private Button dailyButton;
    private Button weeklyButton;
    private EditText rInterval;
    private ChipGroup chipGroupDays;

    private boolean isDaily = false;
    private boolean isWeekly = false;
    private int rIntervalValue = 0;
    private String rIntervalDays = "";


    public RecurringTodoFragment(DatePickerDialog.OnDateSetListener onDateSetListener, Calendar c) {
        this.onDateSetListener = onDateSetListener;
        this.cRecurring = c;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.todo_recurring_dialog, null);

        builder.setView(view)
            .setPositiveButton(R.string.confirm_recurring, (dialogInterface, i) -> {
                String rIntVal = String.valueOf(rInterval.getText());
                if (!rIntVal.equals("")) {
                    rIntervalValue = Integer.parseInt(String.valueOf(rInterval.getText()));
                    Log.i(TAG, "onCreateDialog: exiting dialog");
                    Log.i(TAG, String.valueOf(rIntervalValue));
                }

                setDatePicker(getContext(), onDateSetListener, rIntervalValue, rIntervalDays);
            })
            .setNegativeButton(R.string.cancel, (dialogInterface, i) -> { resetDate();dismiss(); });


        dailyButton = view.findViewById(R.id.button_daily);
        weeklyButton = view.findViewById(R.id.button_weekly);
        chipGroupDays = view.findViewById(R.id.chipgroup_days);
        rInterval = view.findViewById(R.id.recurring_interval);

        dailyButton.setOnClickListener(v -> {
            isDaily = !isDaily;
            if (isWeekly) {
                isWeekly = false;
                chipGroupDays.setVisibility(View.INVISIBLE);
            }

            if (isDaily) {
                rInterval.setVisibility(View.VISIBLE);
            } else {
                rInterval.setVisibility(View.INVISIBLE);
            }
        });

        weeklyButton.setOnClickListener(v -> {
            isWeekly = !isWeekly;
            if (isDaily) {
                isDaily = false;
            }

            if (isWeekly) {
                chipGroupDays.setVisibility(View.VISIBLE);
                rInterval.setVisibility(View.VISIBLE);
            } else {
                chipGroupDays.setVisibility(View.INVISIBLE);
                rInterval.setVisibility(View.INVISIBLE);
            }
        });


        return builder.create();
    }
}
