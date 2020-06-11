package com.ad340.whatdo;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
    private boolean isDaily = false;
    private boolean isWeekly = false;
    private EditText recurringInterval;
    private ChipGroup chipGroupDays;


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
        View view = inflater.inflate(R.layout.todo_recurring_dialog, null);
        builder.setView(view)
        .setTitle(R.string.recurring_tast_title)
        .setPositiveButton(R.string.confirm_recurring, (dialogInterface, i) -> {
            setDatePicker(getContext(), onDateSetListener);
        })
        .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
            resetDate();
            dismiss();
        });


        dailyButton = view.findViewById(R.id.button_daily);
        weeklyButton = view.findViewById(R.id.button_weekly);
        chipGroupDays = view.findViewById(R.id.chipgroup_days);
        recurringInterval = view.findViewById(R.id.recurring_interval);

        dailyButton.setOnClickListener(v -> {
                    if (isWeekly) {
                        isWeekly = false;
                    }
                    isDaily = !isDaily;
//            Log.i(TAG, "onClick: daily");
//            Log.i(TAG, "-- daily");
//            Log.i(TAG, String.valueOf(isDaily));
//            Log.i(TAG, "-- weekly");
//            Log.i(TAG, String.valueOf(isWeekly));


            if (isDaily) {
                recurringInterval.setVisibility(View.VISIBLE);
                Log.i(TAG, "onCreateDialog: ");
                Log.i(TAG, String.valueOf(recurringInterval.getHeight()));


            } else {
                recurringInterval.setVisibility(View.GONE);
            }
        });



        weeklyButton.setOnClickListener(v -> {
            if (isDaily) {
                isDaily = false;
            }
            isWeekly = !isWeekly;
//            Log.i(TAG, "onClick: weekly");
//            Log.i(TAG, "-- daily");
//            Log.i(TAG, String.valueOf(isDaily));
//            Log.i(TAG, "-- weekly");
//            Log.i(TAG, String.valueOf(isWeekly));

            if (isWeekly) {
                recurringInterval.setVisibility(View.VISIBLE);
            } else {
                recurringInterval.setVisibility(View.GONE);
            }
        });



        return builder.create();
    }
}
