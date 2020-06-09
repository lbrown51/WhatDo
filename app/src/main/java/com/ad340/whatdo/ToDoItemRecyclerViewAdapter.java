package com.ad340.whatdo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.ad340.whatdo.PickerUtils.setDatePickerShowOnClick;
import static com.ad340.whatdo.PickerUtils.onDateSetListener;
import static com.ad340.whatdo.PickerUtils.onTimeSetListener;
import static com.ad340.whatdo.PickerUtils.setTimePickerShowOnClick;


public class ToDoItemRecyclerViewAdapter
        extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ToDoItemViewHolder>{

    private static final String TAG = ToDoItemRecyclerViewAdapter.class.getName();
    private Context context;
    private List<Todo> allTodos;
    private Todo[] allTodosArray;
    private List<Todo> todos;
    private Todo[] todoArray;
    private TodoViewModel mTodoViewModel;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;
    private OnTodoInteractionListener listener;


    ToDoItemRecyclerViewAdapter(Context context) {
        this.context = context;
        listener = (OnTodoInteractionListener) this.context;
        mTodoViewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                .get(TodoViewModel.class);
        Log.e(TAG, "1-arg constructor called for adapter");
    }

    // Overloaded this for testing purposes
    ToDoItemRecyclerViewAdapter(Context context, int overload) {
        this(context);
        todos = (List<Todo>) new ArrayList<Todo>();
        allTodos = (List<Todo>) new ArrayList<Todo>();
        // the next section of code doesn't run at all :(
        mTodoViewModel.getAllTodos((List<Todo> newTodos) -> {
            Log.e(TAG, "alt adapter constructor starting");
            todoArray = new Todo[newTodos.size()];
            allTodosArray = new Todo[newTodos.size()];
            for (int i = 0; i < newTodos.size(); i++) {
                Todo ithTodo = newTodos.get(i);
                if (!this.todos.contains(ithTodo)) {
                    this.todos.add(ithTodo);
                    this.allTodos.add(ithTodo);
                    // this is the ONLY place allTodos gets modified
                    allTodosArray[i] = ithTodo;
                    todoArray[i] = ithTodo;
                    Log.e(TAG, "adapter constructor adding " + ithTodo.getTitle());
                }
            }
            // Cast throws exception
            //todoArray = (Todo[]) todos.toArray();
            Log.e(TAG, "alt adapter constructor complete");
        });
        Log.e(TAG, "2-arg adapter constructor called");
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
            // REMOVE THIS LOG - doesn't have date, can't compare?
            //Log.e(TAG, todo.getDate());
            Calendar c = Calendar.getInstance();
            final boolean isExpanded = position==mExpandedPosition;

            // user sets date in DatePicker
            final DatePickerDialog.OnDateSetListener date = onDateSetListener(c, todo, holder, listener);

            // user sets time in TimePicker
            final TimePickerDialog.OnTimeSetListener time = onTimeSetListener(c, todo, holder, listener);

            holder.todoDetail.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.itemView.setActivated(isExpanded);
            holder.toDoTaskName.setText(todo.getTitle());
            holder.toDoDate.setText(todo.getDate());
            holder.toDoTime.setText(todo.getTime());

            holder.toDoFinishedCheckbox.setChecked(false);
            holder.toDoFinishedCheckbox.setOnClickListener(view -> {
                mTodoViewModel.updateTodo(todo, "", 5);
                notifyDataSetChanged();
            });

            holder.rescheduleButton.setOnClickListener(view -> {
                //make popup menu
                PopupMenu popup = new PopupMenu(context, holder.rescheduleButton);
                //inflating menu
                popup.inflate(R.menu.submit_menu);
                //add click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.reschedule:
                            new DatePickerDialog(context, date,
                                    c.get(Calendar.YEAR),
                                    c.get(Calendar.MONTH),
                                    c.get(Calendar.DAY_OF_MONTH)).show();
                            break;
                        case R.id.cancel:
                            mTodoViewModel.removeTodo(todo);
                            mExpandedPosition = isExpanded ? -1:position;
                            notifyItemChanged(previousExpandedPosition);
                            notifyItemChanged(position);
                            break;
                    }
                    return false;
                });
                //displaying the popup
                popup.show();
            });

            // show DatePicker and TimePicker
            setDatePickerShowOnClick(context, c, holder.toDoDateButton, date);
            setTimePickerShowOnClick(context, c, holder.toDoTimeButton, time);

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
        // won't be edited until next setTodos call
        this.allTodos = (List<Todo>) new ArrayList<Todo>();
        this.allTodos.addAll(todos);
        allTodosArray = (Todo[]) todos.toArray(new Todo[todos.size()]);
        // filtered list
        this.todos = (List<Todo>) new ArrayList<Todo>();
        this.todos.addAll(todos);
        todoArray = allTodosArray;

        Log.e(TAG, "setTodos called");
        resetArray();
        notifyDataSetChanged();
    }

    void filterTodosByDate(Calendar startDate, Calendar endDate) throws ParseException {

        Log.e("in filterTodosByDate: ", "range start: " + ToDoItemRecyclerViewAdapter.dateToString(startDate.getTime()));
        Log.e("in filterTodosByDate: ", "range end: " + ToDoItemRecyclerViewAdapter.dateToString(endDate.getTime()));

        Calendar tempCal = Calendar.getInstance();
        // Get original list of todos
        //String[] tempList = Arrays.stream((ArrayList<Todo>) todos).map(p -> p.getDate()).toArray(size -> new String[todos.size()]);
        if (allTodos != null) {
            for (int i = 0; i < allTodosArray.length; i++) {
                // Calendar is saved to task via DateFormat_SHORT
                SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.YY");
                Todo currTodo = allTodosArray[i];
                Date date = sdf.parse(currTodo.getDate());
                tempCal.setTime(date);
                if (todos.contains(currTodo)) {
                    if (tempCal.compareTo(startDate) < 0 || tempCal.compareTo(endDate) > 0) {
                        todos.remove(currTodo);
                        Log.e(TAG, "removed " + currTodo.getTitle() + ", from " + currTodo.getDate());
                    } // end out of range if-statement
                    else {
                        Log.e(TAG, "already contains " + currTodo.getTitle() + ", from " + currTodo.getDate());
                    }
                } // end contains() if-statement
                else { // does not already contain currTodo
                    if (tempCal.compareTo(startDate) >= 0 && tempCal.compareTo(endDate) <= 0) {
                        todos.add(currTodo);
                        Log.e(TAG, "re-adding " + currTodo.getTitle() + ", from " + currTodo.getDate());
                    } // end within range if-statement
                    else {
                        Log.e(TAG, "not adding " + currTodo.getTitle() + ", from " + currTodo.getDate());
                    }
                } // end this loop
            } // end adding/removing
        } else {
            Log.e(TAG, "allTodos is null");
            resetArray();
        }
        //todos = (List<Todo>) tempList;
        Log.e(TAG, "filtered todos from " + calToString(startDate) + " to " + calToString(endDate));
        notifyDataSetChanged();
    }

    public static String calToString(Calendar cal) {
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.YY");
        return dateFormat.format(date);
    }

    public static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.YY");
        return dateFormat.format(date);
    }

    private void resetArray() {
        todoArray = new Todo[todos.size()];
        for (int i = 0; i < todoArray.length; i++) {
            todoArray[i] = todos.get(i);
        }
    }


    static class ToDoItemViewHolder extends RecyclerView.ViewHolder {
        TextView toDoTaskName;
        TextView toDoTime;
        TextView toDoDate;
        ConstraintLayout todoDetail;
        ImageButton rescheduleButton;
        ImageButton toDoDateButton;
        ImageButton toDoTimeButton;
        CheckBox toDoFinishedCheckbox;

        ToDoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoTaskName = itemView.findViewById(R.id.name_text);
            toDoTime = itemView.findViewById(R.id.time_text);
            toDoDate = itemView.findViewById(R.id.date_text);
            todoDetail = itemView.findViewById(R.id.todo_detail);
            toDoFinishedCheckbox = itemView.findViewById(R.id.todo_item_finished_checkbox);
            rescheduleButton = itemView.findViewById(R.id.reschedule_btn);
            toDoDateButton = itemView.findViewById(R.id.date_btn);
            toDoTimeButton = itemView.findViewById(R.id.time_btn);
        }
    }
}
