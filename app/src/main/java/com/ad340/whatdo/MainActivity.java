package com.ad340.whatdo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.ad340.whatdo.PickerUtils.onDateSetListener;
import static com.ad340.whatdo.PickerUtils.onTimeSetListener;
import static com.ad340.whatdo.PickerUtils.setDatePicker;
import static com.ad340.whatdo.PickerUtils.setTimePickerShowOnClick;

public class MainActivity extends AppCompatActivity implements OnTodoInteractionListener, TagListener {

    private static final String TAG = MainActivity.class.getName();
    private MaterialToolbar header;
    private TodoViewModel mTodoViewModel;
    ToDoItemRecyclerViewAdapter adapter;
    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;
    public FloatingActionButton fab;
    private TodoCalendar dateRange;
    private Calendar startDate;
    private Calendar endDate;
    private String tagFilter;
    private TextView viewing;
    TagListDialog tagListDialog;
    TagRecyclerAdapter tagAdapter;
    private StringBuilder recurString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTodoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        mTodoViewModel.emptyListHandler = this;
        adapter = new ToDoItemRecyclerViewAdapter(this);
        RecyclerView toDoRecyclerView = findViewById(R.id.todo_list_recycler_view);

        toDoRecyclerView.setAdapter(adapter);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // DATE FILTRATION
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        tagFilter = "%";

        dateRange = new TodoCalendar(startDate, endDate, tagFilter);
        dateRange.setListener(evt -> {
            mTodoViewModel.getTodosInRange(dateRange);
        });

        viewing = findViewById(R.id.viewing_date_text);

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
        header.setOverflowIcon(getDrawable(R.drawable.ic_baseline_more_horiz_24));
        setSupportActionBar(header);
        viewing = findViewById(R.id.viewing_date_text);

        recurString = new StringBuilder();
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

    private void setAllUpcomingText(TextView view) {
        StringBuilder displayText = new StringBuilder(getString(R.string.all_upcoming));
        view.setText(displayText);
    }

    // VIEW BY DIALOG

//    private void showViewByDialog() {
//        final View createView = View.inflate(this, R.layout.view_by_dialog, null);
//        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(createView);
//
//        ImageButton closeButton = dialog.findViewById(R.id.close_view_by_dialog);
//        closeButton.setOnClickListener((view) -> {
//            view.setVisibility(View.INVISIBLE);
//            dialog.dismiss();
//        });
//
//        dialog.setOnKeyListener((dialogInterface, key, keyEvent) -> {
//            if (key == KeyEvent.KEYCODE_BACK) {
//                dialogInterface.dismiss();
//                return true;
//            } else return false;
//        });
//
//        dialog.show();
//    }

    public void launchSingleDatePicker() {
        //Single Date Picker
        MaterialDatePicker.Builder dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTitleText(R.string.view_by_picker_date_title);
        MaterialDatePicker datePicker = dateBuilder.build();

        datePicker.show(this.getSupportFragmentManager(), datePicker.toString());

        datePicker.addOnCancelListener (DialogInterface::cancel);
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

    public void launchDateRangePicker() {
        //Date Range Picker
        MaterialDatePicker.Builder dateRangeBuilder = MaterialDatePicker.Builder.dateRangePicker();
        dateRangeBuilder.setTitleText(R.string.view_by_picker_date_range_title);
        MaterialDatePicker dateRangePicker = dateRangeBuilder.build();

        dateRangePicker.show(this.getSupportFragmentManager(), dateRangePicker.toString());

        dateRangePicker.addOnCancelListener (DialogInterface::cancel);
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

    public void allUpcomingHandler() {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        setDateMinimum(start);
        end.add(Calendar.YEAR, 1000);
        setAllUpcomingText(viewing);
        dateRange.setDateRange(start, end);
    }

    private void showCreateDialog() {
        final View createView = View.inflate(this, R.layout.create_todo_dialog, null);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(createView);

        StringBuilder dateString = new StringBuilder();
        StringBuilder timeString = new StringBuilder();

        EditText newTodoEditText = dialog.findViewById(R.id.create_todo_task_name_edit_text);
        TextView dateText = dialog.findViewById(R.id.create_todo_date_text);
        TextView timeText = dialog.findViewById(R.id.create_todo_time_text);
        TextView tagText = dialog.findViewById(R.id.create_todo_tag_text);
        Button finishNewTodoButton = dialog.findViewById(R.id.create_todo_finish_btn);
        ImageButton newTodoDateButton = dialog.findViewById(R.id.create_todo_date_btn);
        ImageButton newTodoTimeButton = dialog.findViewById(R.id.create_todo_time_btn);
        ImageButton newTodoNotesButton = dialog.findViewById(R.id.create_todo_notes_btn);
        ImageButton newTodoTagButton = dialog.findViewById(R.id.create_todo_tag_btn);
        ImageView newTodoIsRecurring = dialog.findViewById(R.id.create_todo_is_recurring);
        newTodoIsRecurring.setVisibility(View.INVISIBLE);
        EditText newTodoNotesText = dialog.findViewById(R.id.create_todo_notes_text);

        final DatePickerDialog.OnDateSetListener date = onDateSetListener(dateString, dateText, newTodoIsRecurring, recurString);
        final TimePickerDialog.OnTimeSetListener time = onTimeSetListener(timeString, timeText);

        setDatePicker(this, newTodoDateButton, date);
        setTimePickerShowOnClick(this, newTodoTimeButton, time);

        newTodoNotesButton.setOnClickListener(view -> {
            if (newTodoNotesText.getVisibility() == View.GONE) {
                newTodoNotesText.setVisibility(View.VISIBLE);
            } else {
                newTodoNotesText.setVisibility(View.GONE);
            }
        });

        newTodoTagButton.setOnClickListener(view -> {
            ArrayList<String> tags = mTodoViewModel.getTags();
            PopupMenu popupMenu = new PopupMenu(this, newTodoTagButton);

            popupMenu.getMenu().add(Menu.NONE, 1, 1, R.string.add_new_tag);

            for (int i = 0; i < 5 && i < tags.size(); i++) {
                popupMenu.getMenu().add(Menu.NONE, i + 1, i + 1, tags.get(i));
            }

            if (tags.size() > 0) {
                popupMenu.getMenu().add(Menu.NONE, tags.size() + 2, tags.size() + 2, R.string.show_all_tags);
            }
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "Add New Tag":
                        showAddTodoDialog(tagText);
                        break;
                    case "Show All Tags":
                        launchAllTags(Constants.TAG_CREATE, tagText, null);
                        break;
                    default:
                        tagText.setText(item.getTitle().toString());
                        mTodoViewModel.updateTag(item.getTitle().toString());
                        break;
                }
                return false;
            });
            popupMenu.show();
        });

        finishNewTodoButton.setOnClickListener(view -> {
            String newTodoText = newTodoEditText.getText().toString();
            Calendar c = Calendar.getInstance();

            if (newTodoText.isEmpty()) {
                newTodoEditText.setError(getString(R.string.empty_task_error));
            } else {
                DateFormat df = new SimpleDateFormat("MM/dd/yy", Locale.US);
                try {
                    Date d = df.parse(String.valueOf(dateString));
                    c.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Todo newTodo = new Todo(null, newTodoText, c,
                        String.valueOf(timeString), String.valueOf(newTodoNotesText.getText()),
                        false, String.valueOf(tagText.getText()), this.recurString.toString());
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

    void showAddTodoDialog(TextView tagText) {
        final View tagView = View.inflate(this, R.layout.add_tag_dialog, null);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
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
            tagText.setText(tag);
            dialog.dismiss();
        });

        tagClose.setOnClickListener(v -> {
            dialog.dismiss();
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

    @Override
    public void emptyList() {
        adapter.emptyList();
    }

    @Override
    public void onPause() {
        mTodoViewModel.saveTags();
        super.onPause();
    }

    @Override
    public void launchAllTags(int requestCode, @Nullable TextView newTodoTagText, @Nullable Todo todo) {
        Log.d(TAG, "launchTagFilter: " + mTodoViewModel.getTags());
        tagAdapter = new TagRecyclerAdapter(mTodoViewModel.getTags(), requestCode, newTodoTagText, todo, this);
        tagListDialog = new TagListDialog(this, tagAdapter);
        tagAdapter.setTags(mTodoViewModel.getTags());
        tagListDialog.show();
    }

    @Override
    public void tagListSelect(String tag, int requestCode, @Nullable TextView newTodoTagText, @Nullable Todo todo) throws ParseException {
        switch (requestCode) {
            case Constants.TAG_FILTER:
                dateRange.setTag(tag);
                tagListDialog.dismiss();
                break;
            case Constants.TAG_ADD:
                mTodoViewModel.updateTodo(todo, tag, Constants.TAG);
                mTodoViewModel.updateTag(tag);
                tagListDialog.dismiss();
                break;
            case Constants.TAG_CREATE:
                newTodoTagText.setText(tag);
                mTodoViewModel.updateTag(tag);
                tagListDialog.dismiss();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.single_day_filter:
                launchSingleDatePicker();
                return true;
            case R.id.date_range_filter:
                launchDateRangePicker();
                return true;
            case R.id.all_upcoming_filter:
                allUpcomingHandler();
                return true;
            case R.id.tag_filter:
                launchAllTags(Constants.TAG_FILTER, null, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
