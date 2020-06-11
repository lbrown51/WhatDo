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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;
import java.util.List;
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
    private String rEncoded = "";


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
                StringBuilder encodedString = new StringBuilder();


                if (!rIntVal.equals("")) {
                    if (isDaily) {
                        encodedString.append("D");
                    } else {
                        encodedString.append("W");
                    }
                    rIntervalValue = Integer.parseInt(String.valueOf(rInterval.getText()));
                    Log.i(TAG, "onCreateDialog: chip tags");
                    List<Integer> chipIds = chipGroupDays.getCheckedChipIds();
                    for (int j = 0; j < chipIds.size(); j++) {
                        Chip chip = view.findViewById(chipIds.get(j));
                        Log.i(TAG, String.valueOf(chip.getTag()));
                    }
                    setDatePicker(getContext(), onDateSetListener, rEncoded);
                }
            })
            .setNegativeButton(R.string.cancel, (dialogInterface, i) -> { resetDate();dismiss(); });

        dailyButton = view.findViewById(R.id.button_daily);
        weeklyButton = view.findViewById(R.id.button_weekly);
        chipGroupDays = view.findViewById(R.id.recurring_chipgroup);
        rInterval = view.findViewById(R.id.recurring_interval);

        dailyButton.setOnClickListener(v -> {
            isDaily = true;
            if (isWeekly) {
                isWeekly = false;
                chipGroupDays.setVisibility(View.INVISIBLE);
                chipGroupDays.clearCheck();
            }

            rInterval.setVisibility(View.VISIBLE);
        });

        weeklyButton.setOnClickListener(v -> {
            isWeekly = true;
            if (isDaily) {
                isDaily = false;
            }

            chipGroupDays.setVisibility(View.VISIBLE);
            rInterval.setVisibility(View.VISIBLE);
        });

        return builder.create();
    }
}
