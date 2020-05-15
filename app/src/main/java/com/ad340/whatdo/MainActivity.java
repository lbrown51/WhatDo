package com.ad340.whatdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Layout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ToDoItem> toDoList;
    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView toDoRecyclerView = findViewById(R.id.todo_list_recycler_view);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        toDoList = new ArrayList<>();
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));

        ToDoItemRecyclerViewAdapter adapter = new ToDoItemRecyclerViewAdapter(toDoList);
        toDoRecyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.large_item_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.small_item_spacing);
        toDoRecyclerView.addItemDecoration(new ToDoItemDecoration(largePadding, smallPadding));

        dateView = findViewById(R.id.todays_date);
        Calendar today = Calendar.getInstance();
        dateView.setText(
                String.format("%02d - %02d - %04d",
                        today.get(Calendar.DAY_OF_MONTH),
                        today.get(Calendar.MONTH ) + 1,
                        today.get(Calendar.YEAR)));
    }
}
