package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ToDoItemRecyclerViewAdapter
        extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ToDoItemViewHolder>{
    private Context context;
    private List<Todo> todos;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;
    private static final String TAG = ToDoItemRecyclerViewAdapter.class.getName();
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
        Calendar myCalendar = Calendar.getInstance();

        if (todos != null && position < todos.size()) {
            Todo todo = todos.get(position);
            holder.toDoTaskName.setText(todo.getTitle());

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


            final boolean isExpanded = position==mExpandedPosition;
            holder.todoDetail.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.itemView.setActivated(isExpanded);

            // start working here...
            final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            };

            holder.toDoDateButton.setOnClickListener(view -> {
                new DatePickerDialog(context, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                String myFormat = "MMM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                holder.toDoDate.setText(sdf.format(myCalendar.getTime()));
            });
            previousExpandedPosition = position;
            Todo prevTodo = todos.get(previousExpandedPosition);

            holder.toDoTaskName.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1:position;
                if (mExpandedPosition == -1) {
                    Log.i(TAG, "onBindViewHolder: Hello there!");
                    // grab the previous thing...
                }

                StringBuilder openClick = new StringBuilder("Previous Click Pos: ");
                StringBuilder closeClick = new StringBuilder("Current Click Pos: ");
                openClick.append(previousExpandedPosition);
                closeClick.append(mExpandedPosition);

                Log.i(TAG, "onBindViewHolder: ");
                Log.i(TAG, String.valueOf(openClick));
                Log.i(TAG, String.valueOf(closeClick));

                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            });

            holder.toDoTime.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1:position;
                if (mExpandedPosition == -1) {
                    Log.i(TAG, "onBindViewHolder: Hello there!");
                    Log.i(TAG, (String) holder.toDoTaskName.getText());
                    Log.i(TAG, (String) holder.toDoTaskName.getText());
                }
                StringBuilder openClick = new StringBuilder("Previous Click Pos: ");
                StringBuilder closeClick = new StringBuilder("Current Click Pos: ");
                openClick.append(previousExpandedPosition);
                closeClick.append(mExpandedPosition);

                Log.i(TAG, "onBindViewHolder: ");
                Log.i(TAG, String.valueOf(openClick));
                Log.i(TAG, String.valueOf(closeClick));

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



    public class ToDoItemViewHolder extends RecyclerView.ViewHolder {
        public TextView toDoTaskName;
        public TextView toDoTime;
        public TextView toDoDate;
        ConstraintLayout todoDetail;
        public ImageButton rescheduleButton;
        public ImageButton toDoDateButton;

        public ToDoItemViewHolder(@NonNull View itemView) {
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
