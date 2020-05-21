package com.ad340.whatdo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoItemRecyclerViewAdapter extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ToDoItemViewHolder> {
    private Context context;
    private List<Todo> todos;

    ToDoItemRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ToDoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);

        return new ToDoItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ToDoItemViewHolder holder, int position) {
        if (todos != null && position < todos.size()) {
            Todo todo = todos.get(position);
            holder.toDoTaskName.setText(todo.getTitle());

            boolean isExpanded = todos.get(position).isExpanded();
            holder.todoDetail.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.rescheduleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //make popup menu
                    PopupMenu popup = new PopupMenu(context, holder.rescheduleButton);
                    //inflating menu
                    popup.inflate(R.menu.submit_menu);
                    //add click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.reschedule:
                                    //handle reschedule click
                                    break;
                                case R.id.cancel:
                                    //handle cancel click
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (todos != null)
            return todos.size();
        else
            return 0;
    }

    void setTodos(List<Todo> todos) {
        this.todos = todos;
        notifyDataSetChanged();
    }

    public class ToDoItemViewHolder extends RecyclerView.ViewHolder {
        public TextView toDoTaskName;
        public TextView toDoTime;
        ConstraintLayout todoDetail;
        public ImageButton rescheduleButton;

        public ToDoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoTaskName = itemView.findViewById(R.id.todo_item_task_name);
            todoDetail = itemView.findViewById(R.id.todo_detail);
            toDoTime = itemView.findViewById(R.id.todo_item_time);
            rescheduleButton = itemView.findViewById(R.id.reschedule_btn);

            toDoTaskName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Todo todo = todos.get(getAdapterPosition());
                    todo.setExpanded(!todo.isExpanded());
                    notifyItemChanged(getAdapterPosition()); // calls onBindViewHolder for position
                }
            });

            toDoTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Todo todo = todos.get(getAdapterPosition());
                    todo.setExpanded(!todo.isExpanded());
                    notifyItemChanged(getAdapterPosition()); // calls onBindViewHolder for position
                }
            });
        }
    }
}
