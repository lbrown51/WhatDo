package com.ad340.whatdo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnListInteractionListener {

    private MaterialToolbar header;
    private TodoViewModel mTodoViewModel;
    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;
    public FloatingActionButton fab;
    private static final String TAG = MainActivity.class.getName();
    private OnListInteractionListener mListener;
    private List<Todo> mTodos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListener = (OnListInteractionListener) this;
//        List<Todo> mTodos = mTodoViewModel.getAllTodos().getValue();
        mTodoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        ToDoItemRecyclerViewAdapter adapter = new ToDoItemRecyclerViewAdapter(this, mTodos, mListener);
        RecyclerView toDoRecyclerView = findViewById(R.id.todo_list_recycler_view);

        toDoRecyclerView.setAdapter(adapter);

        mTodoViewModel.getAllTodos().observe(this, todos -> {
            adapter.setTodos(todos);
        });

        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        int largePadding = getResources().getDimensionPixelSize(R.dimen.large_item_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.small_item_spacing);
        toDoRecyclerView.addItemDecoration(new ToDoItemDecoration(largePadding, smallPadding));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
                showCreateDialog();
            });

//        onActivityResult(1, 1, data);


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

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == NEW_TODO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            Todo todo = new Todo()
//        }
//    }

    private void showCreateDialog() {
        final View createView = View.inflate(this, R.layout.create_todo_dialog, null);

        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(createView);

        EditText newTodoEditText = dialog.findViewById(R.id.create_todo_task_name_edit_text);
        Button finishNewTodoButton = dialog.findViewById(R.id.create_todo_finish_btn);
        finishNewTodoButton.setOnClickListener(view -> {
            String newTodoText = newTodoEditText.getText().toString();
            if (newTodoText.isEmpty()) {
                newTodoEditText.setError("Cannot make an empty task");
            } else {
                Todo newTodo = new Todo(null, newTodoText, null, null, null);

                LiveData<List<Todo>> todos = mTodoViewModel.getAllTodos();
                Todo todo = todos.getValue().get(0);
                Log.i(TAG, "showCreateDialog:");
                Log.i(TAG, todo.getTime() != null ? todo.getTime() : "no time");
                mTodoViewModel.insert(newTodo);
                Log.i(TAG, todo.getTime() != null ? todo.getTime() : "no time");

                mTodoViewModel.updateTodo(todo, "Hello");
                dialog.dismiss();
            }
        });


        ImageButton closeButton = (ImageButton) dialog.findViewById(R.id.close_dialog);
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
    };

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");
    }

    @Override
    public void onDestroy() {
//        unregisterReceiver(batteryInfoReceiver);
        super.onDestroy();
    }


    @Override
    public void onListInteraction(Todo todo) {
        Log.i(TAG, "onListInteraction: ");
        Log.i(TAG, todo.getDate() != null ? todo.getDate() : "No date");
        String date = todo.getDate() != null ? todo.getDate() : "";
        mTodoViewModel.updateTodo(todo, date);
    }
}
