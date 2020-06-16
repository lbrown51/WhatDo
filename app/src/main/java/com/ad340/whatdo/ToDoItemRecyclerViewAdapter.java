package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.ad340.whatdo.PickerUtils.setDatePicker;
import static com.ad340.whatdo.PickerUtils.onDateSetListener;
import static com.ad340.whatdo.PickerUtils.onTimeSetListener;
import static com.ad340.whatdo.PickerUtils.setTimePickerShowOnClick;


public class ToDoItemRecyclerViewAdapter
        extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ToDoItemViewHolder> {
    private static final String TAG = ToDoItemRecyclerViewAdapter.class.getName();
    private Context context;
    private List<Todo> todos;
    private TodoViewModel mTodoViewModel;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;
    private OnTodoInteractionListener listener;
    private ArrayList<String> tags;


    ToDoItemRecyclerViewAdapter(Context context) {
        this.context = context;
        listener = (OnTodoInteractionListener) this.context;
        mTodoViewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                .get(TodoViewModel.class);
        tags = mTodoViewModel.getTags();
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
            // date comes in as Date, need to change to string to update
            final boolean isExpanded = position==mExpandedPosition;

            // user sets date in DatePicker
            final DatePickerDialog.OnDateSetListener date = onDateSetListener(todo, holder, listener);

            // user sets time in TimePicker
            final TimePickerDialog.OnTimeSetListener time = onTimeSetListener(todo, holder, listener);

            holder.todoDetail.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.itemView.setActivated(isExpanded);
            holder.toDoTaskName.setText(todo.getTitle());
            holder.toDoDate.setText(new StringBuilder(DateFormat.getDateInstance(DateFormat.FULL).format(todo.getDate().getTime())));
            holder.toDoTime.setText(todo.getTime());
            holder.toDoNotesText.setText(todo.getNotes());
            holder.toDoTag.setText(todo.getTag());
            if (todo.getNotes() == null || todo.getNotes().equals("")) {
                holder.toDoNotesText.setVisibility(View.GONE);
            } else {
                holder.toDoNotesText.setVisibility(View.VISIBLE);
            }
            holder.toDoFinishedCheckbox.setChecked(false);

            holder.toDoFinishedCheckbox.setOnClickListener(view -> {
                try {
                    mTodoViewModel.updateTodo(todo, "", Constants.COMPLETE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent updateWidgetIntent = new Intent(context, TodoListWidget.class);
                updateWidgetIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                int ids[] = AppWidgetManager
                        .getInstance(context.getApplicationContext())
                        .getAppWidgetIds(
                                new ComponentName(context.getApplicationContext(), TodoListWidget.class)
                        );
                updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                context.sendBroadcast(updateWidgetIntent);
                notifyDataSetChanged();
            });

            holder.toDoTagButton.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, holder.toDoTagButton);

                popupMenu.getMenu().add(Menu.NONE, 1, 1, "Add New Tag");

                for (int i = 0; i < 5 && i < tags.size(); i++) {
                    popupMenu.getMenu().add(Menu.NONE, i + 1, i + 1, tags.get(i));
                }

                if (tags.size() > 0) {
                    popupMenu.getMenu().add(Menu.NONE, tags.size() + 2, tags.size() + 2, "See All Tags");
                }
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getTitle().toString()) {
                        case "Add New Tag":
                            Log.d(TAG, "onBindViewHolder: add tag");
                            showAddTodoDialog(view, todo);
                            break;
                        case "See All Tags":
                            break;
                        default:
                            try {
                                mTodoViewModel.updateTodo(todo, item.getTitle().toString(), Constants.TAG);
                                mTodoViewModel.updateTag(item.getItemId() - 1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    return false;
                });
                popupMenu.show();

            });

            holder.rescheduleButton.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(context, holder.rescheduleButton);
                popup.inflate(R.menu.submit_menu);
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.reschedule:
                            setDatePicker(context, date);
                            break;
                        case R.id.cancel:
                            mTodoViewModel.removeTodo(todo);
                            mExpandedPosition = isExpanded ? -1:position;
                            notifyItemChanged(previousExpandedPosition);
                            notifyItemChanged(position);

                            Intent updateWidgetIntent = new Intent(context, TodoListWidget.class);
                            updateWidgetIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                            int ids[] = AppWidgetManager
                                    .getInstance(context.getApplicationContext())
                                    .getAppWidgetIds(
                                            new ComponentName(context.getApplicationContext(), TodoListWidget.class)
                                    );
                            updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                            context.sendBroadcast(updateWidgetIntent);

                            break;
                    }
                    return false;
                });
                //displaying the popup
                popup.show();
            });

            holder.toDoNotesButton.setOnClickListener(
                    view -> toggleNotes(holder.toDoNotesText));

            holder.toDoNotesText.setOnFocusChangeListener((view, b) -> {
                if (!b) {
                    try {
                        listener.onUpdateTodo(
                                todo, String.valueOf(holder.toDoNotesText.getText()), Constants.NOTES);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            // show DatePicker and TimePicker
            setDatePicker(context, holder.toDoDateButton, date);
            setTimePickerShowOnClick(context, holder.toDoTimeButton, time);

            // if current task is expanded, previous = current
            if (isExpanded) previousExpandedPosition = position;


            // listener on Title TextView
            holder.toDoTaskName.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1:position;
                holder.toDoNotesText.setVisibility(View.GONE);
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            });

            // listener on time TextView
            holder.toDoTime.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1:position;
                holder.toDoNotesText.setVisibility(View.GONE);
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            });

            holder.toDoTag.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1:position;
                holder.toDoNotesText.setVisibility(View.GONE);
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
        this.todos = new ArrayList<>();
        this.todos.addAll(todos);
        notifyDataSetChanged();
    }

    void showAddTodoDialog(View view, Todo todo) {
        final View tagView = View.inflate(context, R.layout.add_tag_dialog, null);
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(tagView);

        EditText tagEdit = dialog.findViewById(R.id.add_tag_edit_text);
        Button addTag = dialog.findViewById(R.id.add_tag_finish_btn);
        ImageButton tagClose = dialog.findViewById(R.id.close_tag_dialog);

        tagClose.setOnClickListener((v) -> {
            v.setVisibility(View.INVISIBLE);
            dialog.dismiss();
        });

        addTag.setOnClickListener(v -> {
            String tag = tagEdit.getText().toString();
            mTodoViewModel.addTag(tag);
            try {
                mTodoViewModel.updateTodo(todo, tag, Constants.TAG);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        });

        tagClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    void toggleNotes(EditText notesText) {

        if (notesText.getVisibility() == View.GONE) {
            notesText.setVisibility(View.VISIBLE);
        } else if (String.valueOf(notesText.getText()).equals("")){
            notesText.setVisibility(View.GONE);
        }
    }

    public void emptyList() {
        this.todos = new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ToDoItemViewHolder extends RecyclerView.ViewHolder {
        TextView toDoTaskName;
        TextView toDoTime;
        TextView toDoDate;
        TextView toDoTag;
        ConstraintLayout todoDetail;
        ImageButton rescheduleButton;
        ImageButton toDoDateButton;
        ImageButton toDoTimeButton;
        ImageButton toDoNotesButton;
        ImageButton toDoTagButton;
        CheckBox toDoFinishedCheckbox;
        EditText toDoNotesText;
        ImageView recurringIV;

        ToDoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoTaskName = itemView.findViewById(R.id.name_text);
            toDoTime = itemView.findViewById(R.id.time_text);
            toDoDate = itemView.findViewById(R.id.date_text);
            toDoTag = itemView.findViewById(R.id.tag_display);
            todoDetail = itemView.findViewById(R.id.todo_detail);
            toDoFinishedCheckbox = itemView.findViewById(R.id.todo_item_finished_checkbox);
            rescheduleButton = itemView.findViewById(R.id.reschedule_btn);
            toDoDateButton = itemView.findViewById(R.id.date_btn);
            toDoTimeButton = itemView.findViewById(R.id.time_btn);
            toDoTagButton = itemView.findViewById(R.id.tag_btn);
            toDoNotesButton = itemView.findViewById(R.id.notes_btn);
            toDoNotesText = itemView.findViewById(R.id.notes_text);
            recurringIV = itemView.findViewById(R.id.is_recurring);
        }
    }
}
