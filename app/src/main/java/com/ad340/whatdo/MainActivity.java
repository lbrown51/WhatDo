package com.ad340.whatdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.ad340.whatdo.PickerUtils.setDatePickerShowOnClick;
import static com.ad340.whatdo.PickerUtils.onDateSetListener;
import static com.ad340.whatdo.PickerUtils.onTimeSetListener;
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
            Log.e(TAG, "propertyChange invoked on dateRange");
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
//        StringBuilder displayText = new StringBuilder(header.getTitle());
//        displayText.append(getString(R.string.text_whitespace));
//        displayText.append(startDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
//        displayText.append(String.format(" %02d, %04d",
//                dateRange.getStartDate().get(Calendar.DAY_OF_MONTH),
//                dateRange.getStartDate().get(Calendar.YEAR)));
//        header.setTitle(displayText);
        header.setOnClickListener(view -> { showViewByDialog(); });
        viewing = findViewById(R.id.viewing_date_text);

        Log.d(TAG, "onCreate invoked");
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
            .append(dateRange.getStartDate().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH))
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
            Log.d("DatePicker Activity", "Dialog was cancelled");
        });
        datePicker.addOnNegativeButtonClickListener ((view) -> {
            view.setVisibility(View.GONE);
            Log.d("DatePicker Activity", "Dialog Negative Button was clicked");
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

            Log.d("DatePicker Activity", "Dialog Positive Button was clicked");
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
            Log.d("DatePicker Activity", "Dialog was cancelled");
        });
        dateRangePicker.addOnNegativeButtonClickListener ((view) -> {
            view.setVisibility(View.GONE);
            Log.d("DatePicker Activity", "Dialog Negative Button was clicked");
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
                Log.d(TAG, "onPositiveButtonClick: " + startDate.getTime().toString() + " " + endDate.getTime().toString());
                dateRange.setDateRange(startDate, endDate);
            }
            //try {
                //Log.e(TAG, "range start: " + ToDoItemRecyclerViewAdapter.dateToString(startDate.getTime()));
                //Log.e(TAG, "range end: " + ToDoItemRecyclerViewAdapter.dateToString(endDate.getTime()));
                //adapter.filterTodosByDate(startDate, endDate);
            setDateRangeText(viewing);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            Log.d("DateRangePicker", "Dialog Positive Button was clicked");
        });
    }

    public void allUpcomingHandler(View rootView) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        setDateMinimum(start);
        end.add(Calendar.YEAR, 1);
        dateRange.setDateRange(start, end);
    }

    // CREATE TODO DIALOG

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

        final DatePickerDialog.OnDateSetListener date = onDateSetListener(c, dateString, dateText);
        final TimePickerDialog.OnTimeSetListener time = onTimeSetListener(c, timeString, timeText);

        setDatePickerShowOnClick(this, c, newTodoDateButton, date);
        setTimePickerShowOnClick(this, c, newTodoTimeButton, time);

        SimpleDateFormat sdf = new SimpleDateFormat("DD.mm.YY");

        finishNewTodoButton.setOnClickListener(view -> {
            String newTodoText = newTodoEditText.getText().toString();
            if (newTodoText.isEmpty()) {
                newTodoEditText.setError("Cannot make an empty task");
            } else {
                c.add(Calendar.SECOND, 1);
                Todo newTodo = new Todo(null, newTodoText, c,
                        String.valueOf(timeString), null, false);
                mTodoViewModel.insert(newTodo);
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
