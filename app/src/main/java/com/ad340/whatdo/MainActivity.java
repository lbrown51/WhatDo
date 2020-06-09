package com.ad340.whatdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.ad340.whatdo.PickerUtils.setDatePickerShowOnClick;
import static com.ad340.whatdo.PickerUtils.onDateSetListener;
import static com.ad340.whatdo.PickerUtils.onTimeSetListener;
import static com.ad340.whatdo.PickerUtils.setTimePickerShowOnClick;

public class MainActivity extends AppCompatActivity implements OnTodoInteractionListener {

    private static final String TAG = MainActivity.class.getName();
    private MaterialToolbar header;
    private TodoViewModel mTodoViewModel;
    ToDoItemRecyclerViewAdapter adapter;
    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;
    public FloatingActionButton fab;
    private Calendar startDate;
    private Calendar endDate;
    private boolean dateFiltered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTodoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        adapter = new ToDoItemRecyclerViewAdapter(this, 1);
        RecyclerView toDoRecyclerView = findViewById(R.id.todo_list_recycler_view);

        toDoRecyclerView.setAdapter(adapter);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTodoViewModel.getAllTodos().observe(this, todos -> {
            this.runOnUiThread(() -> {
                adapter.setTodos(todos);
                if (dateFiltered) {
                    try {
                        adapter.filterTodosByDate(startDate, endDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        });

        int largePadding = getResources().getDimensionPixelSize(R.dimen.large_item_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.small_item_spacing);
        toDoRecyclerView.addItemDecoration(new ToDoItemDecoration(largePadding, smallPadding));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
                showCreateDialog();
            });

        Calendar today = Calendar.getInstance();
        header = findViewById(R.id.top_app_bar);
        StringBuilder displayText = new StringBuilder(header.getTitle());
        displayText.append(getString(R.string.text_whitespace));
        displayText.append(today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        displayText.append(String.format(" %02d, %04d",
                today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.YEAR)));
        header.setTitle(displayText);
        header.setOnClickListener(view -> { showViewByDialog(); });

        // DATE FILTRATION
        startDate = today;
        endDate = Calendar.getInstance();
        Log.d(TAG, "onCreate invoked");
    }

    // VIEW BY DIALOG

    private void showViewByDialog() {
        final View createView = View.inflate(this, R.layout.view_by_dialog, null);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(createView);

        ImageButton closeButton = dialog.findViewById(R.id.close_view_by_dialog);
        closeButton.setOnClickListener((view) -> {
            view.setVisibility(View.INVISIBLE);
            dialog.dismiss();
        });

        dialog.setOnKeyListener((dialogInterface, key, keyEvent) -> {
            if (key == KeyEvent.KEYCODE_BACK) {
                dialogInterface.dismiss();
                return true;
            } else return false;
        });

        dialog.show();
    }

    public void launchSingleDatePicker(View rootView) {
        //Single Date Picker
        MaterialDatePicker.Builder dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTitleText(R.string.view_by_picker_date_title);
        MaterialDatePicker datePicker = dateBuilder.build();

        datePicker.show(this.getSupportFragmentManager(), datePicker.toString());

        datePicker.addOnCancelListener ((dialogInterface) -> {
            dialogInterface.cancel();
            Log.d("DatePicker Activity", "Dialog was cancelled");
        });
        datePicker.addOnNegativeButtonClickListener ((view) -> {
            view.setVisibility(View.GONE);
            Log.d("DatePicker Activity", "Dialog Negative Button was clicked");
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Date inputDate = new Date((Long) selection);
                startDate.setTime(inputDate);
                startDate.add(Calendar.DATE, 1);
                endDate.setTime(inputDate);
                endDate.add(Calendar.DATE, 2);
                try {
                    adapter.filterTodosByDate(startDate, endDate);
                    dateFiltered = true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("DatePicker Activity", "Dialog Positive Button was clicked");
            }
        });
    }

    public void launchDateRangePicker(View rootView) {
        //Date Range Picker
        MaterialDatePicker.Builder dateRangeBuilder = MaterialDatePicker.Builder.dateRangePicker();
        dateRangeBuilder.setTitleText(R.string.view_by_picker_date_range_title);
        MaterialDatePicker dateRangePicker = dateRangeBuilder.build();

        dateRangePicker.show(this.getSupportFragmentManager(), dateRangePicker.toString());

        dateRangePicker.addOnCancelListener ((dialogInterface) -> {
            dialogInterface.cancel();
            Log.d("DatePicker Activity", "Dialog was cancelled");
        });
        dateRangePicker.addOnNegativeButtonClickListener ((view) -> {
            view.setVisibility(View.GONE);
            Log.d("DatePicker Activity", "Dialog Negative Button was clicked");
        });

        dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> dateRange = (Pair<Long, Long>) selection;
                Date inputStartDate = new Date(dateRange.first);
                Date inputEndDate = new Date(dateRange.second);
                Log.e("DateRangePicker", "range start: " + ToDoItemRecyclerViewAdapter.dateToString(inputStartDate));
                Log.e("DateRangePicker", "range end: " + ToDoItemRecyclerViewAdapter.dateToString(inputEndDate));
                if (inputStartDate != null) {
                    startDate.setTime(inputStartDate);
                    startDate.add(Calendar.DATE, 1);
                }
                if (inputEndDate != null) {
                    endDate.setTime(inputEndDate);
                    endDate.add(Calendar.DATE, 1);
                }
                try {
                    // WHY IS THIS GOING THROUGH AS EQUAL???
                    Log.e(TAG, "range start: " + ToDoItemRecyclerViewAdapter.dateToString(startDate.getTime()));
                    Log.e(TAG, "range end: " + ToDoItemRecyclerViewAdapter.dateToString(endDate.getTime()));
                    adapter.filterTodosByDate(startDate, endDate);
                    dateFiltered = true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("DateRangePicker", "Dialog Positive Button was clicked");
            }
        });
    }

    public void allUpcomingHandler(View rootView) {
        // STUB
    }

    // CREATE TODO DIALOG

    private void showCreateDialog() {
        final View createView = View.inflate(this, R.layout.create_todo_dialog, null);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(createView);

        Calendar c = Calendar.getInstance();
        StringBuilder dateString = new StringBuilder();
        StringBuilder timeString = new StringBuilder();

        EditText newTodoEditText = dialog.findViewById(R.id.create_todo_task_name_edit_text);
        TextView dateText = dialog.findViewById(R.id.create_todo_date_text);
        TextView timeText = dialog.findViewById(R.id.create_todo_time_text);
        Button finishNewTodoButton = dialog.findViewById(R.id.create_todo_finish_btn);
        ImageButton newTodoDateButton = dialog.findViewById(R.id.create_todo_date_btn);
        ImageButton newTodoTimeButton = dialog.findViewById(R.id.create_todo_time_btn);

        final DatePickerDialog.OnDateSetListener date = onDateSetListener(c, dateString, dateText);
        final TimePickerDialog.OnTimeSetListener time = onTimeSetListener(c, timeString, timeText);

        setDatePickerShowOnClick(this, c, newTodoDateButton, date);
        setTimePickerShowOnClick(this, c, newTodoTimeButton, time);

        finishNewTodoButton.setOnClickListener(view -> {
            String newTodoText = newTodoEditText.getText().toString();
            if (newTodoText.isEmpty()) {
                newTodoEditText.setError("Cannot make an empty task");
            } else {
                Todo newTodo = new Todo(null, newTodoText, String.valueOf(dateString),
                        String.valueOf(timeString), null, false);
                mTodoViewModel.insert(newTodo);
                dialog.dismiss();
            }
        });

        ImageButton closeButton = dialog.findViewById(R.id.close_dialog);
        closeButton.setOnClickListener((view) -> {
            view.setVisibility(View.INVISIBLE);
            dialog.dismiss();
        });

        dialog.setOnKeyListener((dialogInterface, key, keyEvent) -> {
            if (key == KeyEvent.KEYCODE_BACK) {
                dialogInterface.dismiss();
                return true;
            } else return false;
        });

        dialog.show();
    }

    @Override
    public void onUpdateTodo(Todo todo, String data, int type) {
        mTodoViewModel.updateTodo(todo, data, type);
    }
}
