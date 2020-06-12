package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.ad340.whatdo.PickerUtils.onDateSetListener;
import static com.ad340.whatdo.PickerUtils.onTimeSetListener;
import static com.ad340.whatdo.PickerUtils.setDatePickerShowOnClick;
import static com.ad340.whatdo.PickerUtils.setTimePickerShowOnClick;

public class MainActivity extends AppCompatActivity implements OnTodoInteractionListener {

    private static final String TAG = MainActivity.class.getName();
    private MaterialToolbar header;
    private TodoViewModel mTodoViewModel;
    ToDoItemRecyclerViewAdapter adapter;
    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;
    public FloatingActionButton fab;
    private TodoCalendar dateRange;
    private Calendar startDate;
    private Calendar endDate;
    private TextView viewing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTodoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        adapter = new ToDoItemRecyclerViewAdapter(this);
        RecyclerView toDoRecyclerView = findViewById(R.id.todo_list_recycler_view);

        toDoRecyclerView.setAdapter(adapter);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // DATE FILTRATION
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        dateRange = new TodoCalendar(startDate, endDate);
        dateRange.setListener(evt -> {
            mTodoViewModel.getTodosInRange(dateRange);
        });

        viewing = findViewById(R.id.viewing_date_text);
        setSingleDateText(viewing);

        final Observer<List<Todo>> todoObserver = newTodos -> {
            if (newTodos == null || newTodos.size() <= 0) {
                return;
            }
            adapter.setTodos(newTodos);
        };

        mTodoViewModel.getAllTodos().observe(this, todoObserver);

        setDateMinimum(startDate);
        endDate.add(Calendar.YEAR, 1);

        dateRange.setDateRange(startDate, endDate);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.large_item_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.small_item_spacing);
        toDoRecyclerView.addItemDecoration(new ToDoItemDecoration(largePadding, smallPadding));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
                showCreateDialog();
            });

        header = findViewById(R.id.top_app_bar);
        header.setOnClickListener(view -> { showViewByDialog(); });
        viewing = findViewById(R.id.viewing_date_text);
    }

    // EDIT TEXT BY VIEW BOUNDS

    private void setSingleDateText(TextView view) {
        StringBuilder displayText = new StringBuilder();
        displayText.append(dateRange.getStartDate().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        displayText.append(String.format(" %02d, %04d",
                dateRange.getStartDate().get(Calendar.DAY_OF_MONTH),
                dateRange.getStartDate().get(Calendar.YEAR)));
        view.setText(displayText);
    }

    private void setDateRangeText(TextView view) {
        StringBuilder displayText = new StringBuilder();
        displayText.append(dateRange.getStartDate().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH))
            .append(String.format(" %02d, %04d",
                dateRange.getStartDate().get(Calendar.DAY_OF_MONTH),
                dateRange.getStartDate().get(Calendar.YEAR)))
            .append(" to ")
            .append(dateRange.getEndDate().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH))
            .append(String.format(" %02d, %04d",
                dateRange.getEndDate().get(Calendar.DAY_OF_MONTH),
                dateRange.getEndDate().get(Calendar.YEAR)));

        view.setText(displayText);
    }

    // VIEW BY DIALOG

    private void showViewByDialog() {
        final View createView = View.inflate(this, R.layout.view_by_dialog, null);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(createView);

        ImageButton closeButton = dialog.findViewById(R.id.close_view_by_dialog);
        closeButton.setOnClickListener((view) -> {
            view.setVisibility(View.INVISIBLE);
            dialog.dismiss();
        });

        dialog.setOnKeyListener((dialogInterface, key, keyEvent) -> {
            if (key == KeyEvent.KEYCODE_BACK) {
                dialogInterface.dismiss();
                return true;
            } else return false;
        });

        dialog.show();
    }

    public void launchSingleDatePicker(View rootView) {
        //Single Date Picker
        MaterialDatePicker.Builder dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTitleText(R.string.view_by_picker_date_title);
        MaterialDatePicker datePicker = dateBuilder.build();

        datePicker.show(this.getSupportFragmentManager(), datePicker.toString());

        datePicker.addOnCancelListener ((dialogInterface) -> {
            dialogInterface.cancel();
        });
        datePicker.addOnNegativeButtonClickListener ((view) -> {
            view.setVisibility(View.GONE);
        });

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Date inputDate = new Date((Long) selection);
            startDate.setTime(inputDate);
            startDate.add(Calendar.DATE, 1);
            setDateMinimum(startDate);
            endDate.setTime(inputDate);
            endDate.add(Calendar.DATE, 1);
            setDateMaximum(endDate);
            dateRange.setDateRange(startDate, endDate);
            setSingleDateText(viewing);
        });
    }

    private static void setDateMinimum(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, date.getMinimum(Calendar.HOUR_OF_DAY));
        date.set(Calendar.MINUTE, date.getMinimum(Calendar.MINUTE));
        date.set(Calendar.SECOND, date.getMinimum(Calendar.SECOND));
    }

    private static void setDateMaximum(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, date.getActualMaximum(Calendar.HOUR_OF_DAY));
        date.set(Calendar.MINUTE, date.getActualMaximum(Calendar.MINUTE));
        date.set(Calendar.SECOND, date.getActualMaximum(Calendar.SECOND));
    }

    public void launchDateRangePicker(View rootView) {
        //Date Range Picker
        MaterialDatePicker.Builder dateRangeBuilder = MaterialDatePicker.Builder.dateRangePicker();
        dateRangeBuilder.setTitleText(R.string.view_by_picker_date_range_title);
        MaterialDatePicker dateRangePicker = dateRangeBuilder.build();

        dateRangePicker.show(this.getSupportFragmentManager(), dateRangePicker.toString());

        dateRangePicker.addOnCancelListener ((dialogInterface) -> {
            dialogInterface.cancel();
        });
        dateRangePicker.addOnNegativeButtonClickListener ((view) -> {
            view.setVisibility(View.GONE);
        });

        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            Pair<Long, Long> inputDateRange = (Pair<Long, Long>) selection;
            if (inputDateRange.first != null && inputDateRange.second != null) {
                startDate.setTimeInMillis(inputDateRange.first);
                startDate.add(Calendar.DATE, 1);
                setDateMinimum(startDate);
            }
            if (inputDateRange.second != null) {
                endDate.setTimeInMillis(inputDateRange.second);
                endDate.add(Calendar.DATE, 1);
                setDateMaximum(endDate);
                dateRange.setDateRange(startDate, endDate);
                setDateRangeText(viewing);
            }
        });
    }

    public void allUpcomingHandler(View rootView) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        setDateMinimum(start);
        end.add(Calendar.YEAR, 1);
        dateRange.setDateRange(start, end);
    }

    private void showCreateDialog() {
        final View createView = View.inflate(this, R.layout.create_todo_dialog, null);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(createView);

        Calendar c = Calendar.getInstance();
        StringBuilder dateString = new StringBuilder();
        StringBuilder timeString = new StringBuilder();

        EditText newTodoEditText = dialog.findViewById(R.id.create_todo_task_name_edit_text);
        TextView dateText = dialog.findViewById(R.id.create_todo_date_text);
        TextView timeText = dialog.findViewById(R.id.create_todo_time_text);
        Button finishNewTodoButton = dialog.findViewById(R.id.create_todo_finish_btn);
        ImageButton newTodoDateButton = dialog.findViewById(R.id.create_todo_date_btn);
        ImageButton newTodoTimeButton = dialog.findViewById(R.id.create_todo_time_btn);
        ImageButton newTodoNotesButton = dialog.findViewById(R.id.create_todo_notes_btn);
        EditText newTodoNotesText = dialog.findViewById(R.id.create_todo_notes_text);

        final DatePickerDialog.OnDateSetListener date = onDateSetListener(c, dateString, dateText);
        final TimePickerDialog.OnTimeSetListener time = onTimeSetListener(c, timeString, timeText);

        setDatePickerShowOnClick(this, c, newTodoDateButton, date);
        setTimePickerShowOnClick(this, c, newTodoTimeButton, time);

        newTodoNotesButton.setOnClickListener(view -> {
            if (newTodoNotesText.getVisibility() == View.GONE) {
                newTodoNotesText.setVisibility(View.VISIBLE);
            } else {
                newTodoNotesText.setVisibility(View.GONE);
            }
        });

        finishNewTodoButton.setOnClickListener(view -> {
            String newTodoText = newTodoEditText.getText().toString();
            if (newTodoText.isEmpty()) {
                newTodoEditText.setError(getString(R.string.empty_task_error));
            } else {
                Todo newTodo = new Todo(null, newTodoText, c,
                        String.valueOf(timeString), String.valueOf(newTodoNotesText.getText()),
                        false, null);
                mTodoViewModel.insert(newTodo);

                Intent updateWidgetIntent = new Intent(this, TodoListWidget.class);
                updateWidgetIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                int[] ids = AppWidgetManager
                        .getInstance(getApplicationContext())
                        .getAppWidgetIds(
                                new ComponentName(getApplicationContext(), TodoListWidget.class)
                        );
                updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(updateWidgetIntent);

                dialog.dismiss();
            }
        });

        ImageButton closeButton = dialog.findViewById(R.id.close_dialog);
        closeButton.setOnClickListener((view) -> {
            view.setVisibility(View.INVISIBLE);
            dialog.dismiss();
        });

        dialog.setOnKeyListener((dialogInterface, key, keyEvent) -> {
            if (key == KeyEvent.KEYCODE_BACK) {
                dialogInterface.dismiss();
                return true;
            } else return false;
        });

        dialog.show();
    }

    @Override
    public void onUpdateTodo(Todo todo, String data, int type) {
        try {
            mTodoViewModel.updateTodo(todo, data, type);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
