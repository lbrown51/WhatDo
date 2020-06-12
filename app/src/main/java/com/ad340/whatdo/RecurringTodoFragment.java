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

import java.util.List;
import java.util.Objects;

import static com.ad340.whatdo.PickerUtils.resetDate;
import static com.ad340.whatdo.PickerUtils.setDatePicker;

public class RecurringTodoFragment extends DialogFragment {
    private static final String TAG = RecurringTodoFragment.class.getName();
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private Chip dailyChip;
    private Chip weeklyChip;
    private EditText rInterval;
    private ChipGroup chipGroupDays;


    public RecurringTodoFragment(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.todo_recurring_dialog, null);

        builder.setView(view);

        dailyChip = view.findViewById(R.id.chip_daily);
        weeklyChip = view.findViewById(R.id.chip_weekly);
        Button cancelButton = view.findViewById(R.id.r_cancel_button);
        Button confirmButton = view.findViewById(R.id.r_confirm_button);
        chipGroupDays = view.findViewById(R.id.r_chipgroup_days);
        rInterval = view.findViewById(R.id.r_interval);

        dailyChip.setOnClickListener(v -> {
            rInterval.setHint(R.string.r_interval_daily);
            if (weeklyChip.isChecked()) {
                chipGroupDays.setVisibility(View.INVISIBLE);
                chipGroupDays.clearCheck();
            }
            dailyChip.setChecked(true);
        });

        weeklyChip.setOnClickListener(v -> {
            rInterval.setHint(R.string.r_interval_weekly);
            weeklyChip.setChecked(true);
            chipGroupDays.setVisibility(View.VISIBLE);
        });

        cancelButton.setOnClickListener(v -> {
            resetDate();
            dismiss();
        });

        confirmButton.setOnClickListener(v -> {
            String rIntVal = String.valueOf(rInterval.getText());
            StringBuilder encodedString = new StringBuilder();

            if (validateSelection()) {
                if (dailyChip.isChecked()) {
                    encodedString.append(getString(R.string.RD)).append(rIntVal);
                } else {
                    encodedString.append(getString(R.string.RW))
                            .append(rIntVal).append(getString(R.string.symbol_dash));
                    List<Integer> chipIds = chipGroupDays.getCheckedChipIds();
                    for (int j = 0; j < chipIds.size(); j++) {
                        Chip chip = view.findViewById(chipIds.get(j));
                        encodedString.append(chip.getTag());
                    }
                }
                setDatePicker(getContext(), onDateSetListener, String.valueOf(encodedString));
                dismiss();
            } else {
                Log.i(TAG, "onCreateDialog: UH OHHH");
            }
        });

        return builder.create();
    }

    private boolean validateSelection() {
        boolean isValid = false;
        String rIntVal = String.valueOf(rInterval.getText());
        String selection = dailyChip.isChecked() ? getString(R.string.daily) : getString(R.string.weekly);
        if (rIntVal.equals("")) { // interval empty
            StringBuilder errMsg = new StringBuilder(getString(R.string.must_enter)).append(" ")
                    .append(selection).append(" ").append(getString(R.string.interval));
            rInterval.setError(errMsg);
        } else if (rIntVal.length() > 3) { // interval too big
            StringBuilder errMsg = new StringBuilder(getString(R.string.max_interval));
            rInterval.setError(errMsg);
        } else {
            isValid = true;
        }
        return isValid;
    }
}
