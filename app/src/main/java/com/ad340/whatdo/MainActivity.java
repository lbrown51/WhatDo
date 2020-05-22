package com.ad340.whatdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar header;
    private TodoViewModel mTodoViewModel;
    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView toDoRecyclerView = findViewById(R.id.todo_list_recycler_view);
        ToDoItemRecyclerViewAdapter adapter = new ToDoItemRecyclerViewAdapter(this);
        toDoRecyclerView.setAdapter(adapter);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTodoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        mTodoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                adapter.setTodos(todos);
            }
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
}
