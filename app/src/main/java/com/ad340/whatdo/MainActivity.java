package com.ad340.whatdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Locale;

import static com.ad340.whatdo.PickerUtils.setDatePickerShowOnClick;
import static com.ad340.whatdo.PickerUtils.onDateSetListener;
import static com.ad340.whatdo.PickerUtils.onTimeSetListener;
import static com.ad340.whatdo.PickerUtils.setTimePickerShowOnClick;

public class MainActivity extends AppCompatActivity implements OnTodoInteractionListener {

    private MaterialToolbar header;
    private TodoViewModel mTodoViewModel;
    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTodoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        ToDoItemRecyclerViewAdapter adapter = new ToDoItemRecyclerViewAdapter(this);
        RecyclerView toDoRecyclerView = findViewById(R.id.todo_list_recycler_view);

        toDoRecyclerView.setAdapter(adapter);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTodoViewModel.getUncompletedTodos().observe(this, todos -> {
            adapter.setTodos(todos);
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
        header.setOnClickListener(view -> {
                //Fragment viewByFragment = new ViewByDialog();
                //replaceFragment(viewByFragment);
                showDateRangeDialog();
            });
    }

    private void showDateRangeDialog() {
        final View createView = View.inflate(this, R.layout.view_by_dialog, null);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(createView);


        Button dateBtn = dialog.findViewById(R.id.view_by_date);
        Button dateRangeBtn = dialog.findViewById(R.id.view_by_date_range);
        Button allUpcomingBtn = dialog.findViewById(R.id.view_by_all_upcoming);

//        dateRangeBtn.setOnClickListener(view -> {
//                //make other buttons inactive/gray
//            launchDateRangePicker();
//        });

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

    public void launchDateRangePicker(View rootView) {
        //Date Range Picker
        MaterialDatePicker.Builder dateRangeBuilder = MaterialDatePicker.Builder.dateRangePicker();
        dateRangeBuilder.setTitleText(R.string.view_by_picker_title);
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

        dateRangePicker.addOnPositiveButtonClickListener ((dialogInterface) -> {
            Log.d("DatePicker Activity", "Dialog Positive Button was clicked");
        });
    }


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
