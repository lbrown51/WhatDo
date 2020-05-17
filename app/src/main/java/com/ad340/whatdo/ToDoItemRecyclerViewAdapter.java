package com.ad340.whatdo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoItemRecyclerViewAdapter extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ToDoItemViewHolder> {
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

            boolean isExpanded = toDoList.get(position).isExpanded();
            holder.todoDetail.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public class ToDoItemViewHolder extends RecyclerView.ViewHolder {
        public TextView toDoTaskName;
        ConstraintLayout todoDetail;

        public ToDoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoTaskName = itemView.findViewById(R.id.todo_item_task_name);
            todoDetail = itemView.findViewById(R.id.todo_detail);

            toDoTaskName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToDoItem toDoItem = toDoList.get(getAdapterPosition());
                    toDoItem.setExpanded(!toDoItem.isExpanded());
                    notifyItemChanged(getAdapterPosition()); // calls onBindViewHolder for position
                }
            });
        }
    }
}
