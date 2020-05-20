package com.ad340.whatdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Layout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private List<ToDoItem> toDoList;
    private MaterialToolbar header;
    private TodoViewModel mTodoViewModel;
    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView toDoRecyclerView = findViewById(R.id.todo_list_recycler_view);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTodoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        mTodoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {

            }
        });


        toDoList = new ArrayList<>();
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));

        ToDoItemRecyclerViewAdapter adapter = new ToDoItemRecyclerViewAdapter(toDoList, this);
        toDoRecyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.large_item_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.small_item_spacing);
        toDoRecyclerView.addItemDecoration(new ToDoItemDecoration(largePadding, smallPadding));

        Calendar today = Calendar.getInstance();
        header = findViewById(R.id.top_app_bar);
        StringBuilder displayText = new StringBuilder(header.getTitle());
        // probably a more elegant way of doing this
        // if you save whitespace as a string, it comes out with quotation marks :(
        displayText.append(getString(R.string.text_whitespace));
        displayText.append(today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        displayText.append(String.format(" %02d, %04d",
                today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.YEAR)));
        header.setTitle(displayText);
    }
}
