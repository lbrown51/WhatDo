package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class ToDoItemRecyclerViewAdapter
        extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ToDoItemViewHolder>{
    private Context context;
    private List<Todo> todos;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;
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
            final boolean isExpanded = position==mExpandedPosition;

            // user sets date in DatePicker
            Calendar c = Calendar.getInstance();
            final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String currentDateString = " " + DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

                holder.toDoDate.setText(currentDateString);
                todo.setDate(currentDateString);
            };


            holder.todoDetail.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.toDoTaskName.setText(todo.getTitle());
            holder.toDoDate.setText(todo.getDate());
            holder.rescheduleButton.setOnClickListener(view -> {

                //make popup menu
                PopupMenu popup = new PopupMenu(context, holder.rescheduleButton);
                //inflating menu
                popup.inflate(R.menu.submit_menu);
                //add click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.reschedule:
                            //handle reschedule click
                            break;
                        case R.id.cancel:
                            //handle cancel click
                            break;
                    }
                    return false;
                });
                //displaying the popup
                popup.show();
            });

            holder.itemView.setActivated(isExpanded);

            // show DatePicker
            holder.toDoDateButton.setOnClickListener(view ->
                    new DatePickerDialog(context, date,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show()
            );

            // if current task is expanded...
            if (isExpanded) previousExpandedPosition = position;

            // listener on Title TextView
            holder.toDoTaskName.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            });

            // listener on time TextView
            holder.toDoTime.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
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


    static class ToDoItemViewHolder extends RecyclerView.ViewHolder {
        TextView toDoTaskName;
        TextView toDoTime;
        TextView toDoDate;
        ConstraintLayout todoDetail;
        ImageButton rescheduleButton;
        ImageButton toDoDateButton;

        ToDoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoTaskName = itemView.findViewById(R.id.todo_item_task_name);
            todoDetail = itemView.findViewById(R.id.todo_detail);
            toDoTime = itemView.findViewById(R.id.todo_item_time);
            toDoDate = itemView.findViewById(R.id.date_text);
            rescheduleButton = itemView.findViewById(R.id.reschedule_btn);
            toDoDateButton = itemView.findViewById(R.id.date_btn);
        }
    }
}
