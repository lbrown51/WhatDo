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

    private Button dailyButton;
    private Button weeklyButton;
    private Button cancelButton;
    private Button confirmButton;
    private EditText rInterval;
    private ChipGroup chipGroupDays;

    private boolean isDaily = false;
    private boolean isWeekly = false;



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

        dailyButton = view.findViewById(R.id.button_daily);
        weeklyButton = view.findViewById(R.id.button_weekly);
        cancelButton = view.findViewById(R.id.rec_cancel_button);
        confirmButton = view.findViewById(R.id.rec_confirm_button);
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

        cancelButton.setOnClickListener(v -> {
            resetDate();
            dismiss();
        });

        confirmButton.setOnClickListener(v -> {
            String rIntVal = String.valueOf(rInterval.getText());
            StringBuilder encodedString = new StringBuilder();

            if (!rIntVal.equals("")) {
                if (isDaily) {
                    encodedString.append(getString(R.string.RD)).append(rIntVal);
                } else if (isWeekly) {
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
}
