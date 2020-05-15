package com.ad340.whatdo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoItemRecyclerViewAdapter
        extends RecyclerView.Adapter<ToDoItemViewHolder> {
    private List<ToDoItem> toDoList;

    ToDoItemRecyclerViewAdapter(List<ToDoItem> toDoList) {
        this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public ToDoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);

        return new ToDoItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoItemViewHolder holder, int position) {
        if (toDoList != null && position < toDoList.size()) {
            ToDoItem toDo = toDoList.get(position);
            holder.toDoTaskName.setText(toDo.taskName);
        }

    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }
}
