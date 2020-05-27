package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ToDoItemRecyclerViewAdapter
        extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ToDoItemViewHolder>{
    private static final String TAG = ToDoItemRecyclerViewAdapter.class.getName();
    private Context context;
    private List<Todo> todos;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;
    private OnTodoInteractionListener listener;


    ToDoItemRecyclerViewAdapter(Context context) {
        this.context = context;
        listener = (OnTodoInteractionListener) this.context;
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
            Calendar c = Calendar.getInstance();
            final boolean isExpanded = position==mExpandedPosition;

            // user sets date in DatePicker
            final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                StringBuilder dateString = new StringBuilder(DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()));

                holder.toDoDate.setText(dateString);
                if (listener != null) {
                    Log.i(TAG, "onBindViewHolder: listener");
                    listener.onUpdateTodo(todo, String.valueOf(dateString), Constants.DATE);
                }
            };

            // user sets time in TimePicker
            final TimePickerDialog.OnTimeSetListener time = (view, hourOfDay, minute) -> {
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
                String timeString = sdf.format(c.getTime());

                holder.toDoTime.setText(timeString);
                if (listener != null) {
                    Log.i(TAG, "onBindViewHolder: listener");
                    listener.onUpdateTodo(todo, timeString, Constants.TIME);
                }
            };

            holder.todoDetail.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.itemView.setActivated(isExpanded);
            holder.toDoTaskName.setText(todo.getTitle());
            holder.toDoDate.setText(todo.getDate());
            holder.toDoTime.setText(todo.getTime());
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


            // show DatePicker
            holder.toDoDateButton.setOnClickListener(view ->
                    new DatePickerDialog(context, date,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show()
            );

            // show TimePicker
            holder.toDoTimeButton.setOnClickListener(view ->
                    new TimePickerDialog(context, time,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE), false).show());

            // if current task is expanded, previous = current
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
        ImageButton toDoTimeButton;

        ToDoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoTaskName = itemView.findViewById(R.id.name_text);
            toDoTime = itemView.findViewById(R.id.time_text);
            toDoDate = itemView.findViewById(R.id.date_text);
            todoDetail = itemView.findViewById(R.id.todo_detail);
            rescheduleButton = itemView.findViewById(R.id.reschedule_btn);
            toDoDateButton = itemView.findViewById(R.id.date_btn);
            toDoTimeButton = itemView.findViewById(R.id.time_btn);
        }
    }
}
