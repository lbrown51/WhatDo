package com.ad340.whatdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Locale;

import static com.ad340.whatdo.PickerUtils.setDatePickerShowOnClick;
import static com.ad340.whatdo.PickerUtils.onDateSetListener;
import static com.ad340.whatdo.PickerUtils.onTimeSetListener;
import static com.ad340.whatdo.PickerUtils.setTimePickerShowOnClick;

public class MainActivity extends AppCompatActivity implements OnTodoInteractionListener {
    private static final String TAG = MainActivity.class.getName();
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

        int largePadding = getResources().getDimensionPixelSize(R.dimen.dp_16);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.dp_6);
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
        ImageButton newTodoNotesButton = dialog.findViewById(R.id.create_todo_notes_btn);
        EditText newTodoNotesText = dialog.findViewById(R.id.create_todo_notes_text);

        final DatePickerDialog.OnDateSetListener date = onDateSetListener(c, dateString, dateText);
        final TimePickerDialog.OnTimeSetListener time = onTimeSetListener(c, timeString, timeText);

        setDatePickerShowOnClick(this, c, newTodoDateButton, date);
        setTimePickerShowOnClick(this, c, newTodoTimeButton, time);

        newTodoNotesButton.setOnClickListener(view -> {
            if (newTodoNotesText.getVisibility() == View.GONE) {
                newTodoNotesText.setVisibility(View.VISIBLE);
            } else {
                newTodoNotesText.setVisibility(View.GONE);
            }
        });

        finishNewTodoButton.setOnClickListener(view -> {
            String newTodoText = newTodoEditText.getText().toString();
            if (newTodoText.isEmpty()) {
                newTodoEditText.setError("Cannot make an empty task");
            } else {
                Todo newTodo = new Todo(null, newTodoText, String.valueOf(dateString),
                        String.valueOf(timeString), String.valueOf(newTodoNotesText.getText()),
                        false);
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
