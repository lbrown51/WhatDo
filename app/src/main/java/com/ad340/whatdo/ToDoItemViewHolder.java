package com.ad340.whatdo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoItemViewHolder extends RecyclerView.ViewHolder {
    public TextView toDoTaskName;

    public ToDoItemViewHolder(@NonNull View itemView) {
        super(itemView);
        toDoTaskName = itemView.findViewById(R.id.todo_item_task_name);
    }
}
