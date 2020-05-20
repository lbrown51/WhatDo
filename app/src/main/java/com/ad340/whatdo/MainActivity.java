package com.ad340.whatdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private List<ToDoItem> toDoList;
    private MaterialToolbar header;
    private FloatingActionButton fab;

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
        displayText.append("      ");
        displayText.append(today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        displayText.append(String.format(" %02d, %04d",
                today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.YEAR)));
        header.setTitle(displayText);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new To-Do", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

    }
}
