package com.ad340.whatdo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoItemRecyclerViewAdapter extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ToDoItemViewHolder> {
    private List<ToDoItem> toDoList;
    private Context context;

    ToDoItemRecyclerViewAdapter(List<ToDoItem> toDoList, Context context) {
        this.toDoList = toDoList;
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
        if (toDoList != null && position < toDoList.size()) {
            ToDoItem toDo = toDoList.get(position);
            holder.toDoTaskName.setText(toDo.taskName);

            boolean isExpanded = toDoList.get(position).isExpanded();
            holder.todoDetail.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //make popup menu
                    PopupMenu popup = new PopupMenu(context, holder.submitButton);
                    //inflating menu
                    popup.inflate(R.menu.submit_menu);
                    //add click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mark_complete:
                                    //handle mark_complete click
                                    break;
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
        return toDoList.size();
    }

    public class ToDoItemViewHolder extends RecyclerView.ViewHolder {
        public TextView toDoTaskName;
        public TextView toDoTaskDatetime;
        ConstraintLayout todoDetail;
        public Button submitButton;

        public ToDoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoTaskName = itemView.findViewById(R.id.todo_item_task_name);
            todoDetail = itemView.findViewById(R.id.todo_detail);
            toDoTaskDatetime = itemView.findViewById(R.id.todo_item_due_datetime);
            submitButton = itemView.findViewById(R.id.task_submit_btn);

            toDoTaskName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToDoItem toDoItem = toDoList.get(getAdapterPosition());
                    toDoItem.setExpanded(!toDoItem.isExpanded());
                    notifyItemChanged(getAdapterPosition()); // calls onBindViewHolder for position
                }
            });

            toDoTaskDatetime.setOnClickListener(new View.OnClickListener() {
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
