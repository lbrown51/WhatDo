package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import static com.ad340.whatdo.PickerUtils.setDatePickerShowOnClick;
import static com.ad340.whatdo.PickerUtils.onDateSetListener;
import static com.ad340.whatdo.PickerUtils.onTimeSetListener;
import static com.ad340.whatdo.PickerUtils.setTimePickerShowOnClick;


public class ToDoItemRecyclerViewAdapter
        extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ToDoItemViewHolder>{
    private static final String TAG = ToDoItemRecyclerViewAdapter.class.getName();
    private Context context;
    private List<Todo> todos;
    private TodoViewModel mTodoViewModel;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;
    private OnTodoInteractionListener listener;


    ToDoItemRecyclerViewAdapter(Context context) {
        this.context = context;
        listener = (OnTodoInteractionListener) this.context;
        mTodoViewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                .get(TodoViewModel.class);
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
            final DatePickerDialog.OnDateSetListener date = onDateSetListener(c, todo, holder, listener);

            // user sets time in TimePicker
            final TimePickerDialog.OnTimeSetListener time = onTimeSetListener(c, todo, holder, listener);

            holder.todoDetail.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.itemView.setActivated(isExpanded);
            holder.toDoTaskName.setText(todo.getTitle());
            holder.toDoDate.setText(todo.getDate());
            holder.toDoTime.setText(todo.getTime());
            holder.toDoNotesText.setText(todo.getNotes());
            holder.toDoFinishedCheckbox.setChecked(false);

            holder.toDoFinishedCheckbox.setOnClickListener(view -> {
                mTodoViewModel.updateTodo(todo, "", Constants.COMPLETE);
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

            holder.toDoNotesButton.setOnClickListener(view -> {
                toggleNotes(holder.toDoNotesText, holder.toDoNotesSaveButton);
            });

            holder.toDoNotesSaveButton.setOnClickListener(view -> {
                holder.toDoNotesText.setVisibility(View.GONE);
                holder.toDoNotesSaveButton.setVisibility(View.GONE);
                listener.onUpdateTodo(todo, String.valueOf(holder.toDoNotesText.getText()), Constants.NOTES);
            });

            // show DatePicker and TimePicker
            setDatePickerShowOnClick(context, c, holder.toDoDateButton, date);
            setTimePickerShowOnClick(context, c, holder.toDoTimeButton, time);

            // if current task is expanded, previous = current
            if (isExpanded) previousExpandedPosition = position;

            // listener on Title TextView
            holder.toDoTaskName.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1:position;
                holder.toDoNotesText.setVisibility(View.GONE);
                holder.toDoNotesSaveButton.setVisibility(View.GONE);
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            });

            // listener on time TextView
            holder.toDoTime.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1:position;
                holder.toDoNotesText.setVisibility(View.GONE);
                holder.toDoNotesSaveButton.setVisibility(View.GONE);
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

    void toggleNotes(EditText notesText, ImageButton saveButton) {

        if (notesText.getVisibility() == View.GONE) {
            notesText.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
        } else {
            notesText.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
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
        ImageButton toDoNotesButton;
        ImageButton toDoNotesSaveButton;
        CheckBox toDoFinishedCheckbox;
        EditText toDoNotesText;

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
            toDoNotesButton = itemView.findViewById(R.id.notes_btn);
            toDoNotesText = itemView.findViewById(R.id.notes_text);
            toDoNotesSaveButton = itemView.findViewById(R.id.notes_save_btn);
        }
    }
}
