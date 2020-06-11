package com.ad340.whatdo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TodoViewModel extends AndroidViewModel implements TodoHandler{

    private static final String TAG = TodoViewModel.class.getSimpleName();
    private TodoRepository repository;
    MutableLiveData<TodoCalendar> dateFilter;
    LiveData<List<Todo>> allTodos;
    TodoCalendar currentRange;
    private LiveData<List<Todo>> uncompletedTodos;

    public TodoViewModel(Application application) {
        super(application);
        dateFilter = new MutableLiveData<>();
        repository = new TodoRepository(application, this);
        allTodos = Transformations.switchMap(dateFilter, filter -> repository.getTodosInRange(filter.getStartDate(), filter.getEndDate()));

        uncompletedTodos = repository.getUncompletedTodos();
    }

    LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }

    @Override
    public void getTodosInRange(@Nullable TodoCalendar range) {
        if (range != null) {
            currentRange = range;
        }
        dateFilter.postValue(currentRange);
    }

    //LiveData<List<Todo>> getAllTodos(Consumer<List<Todo>> responseCallback) { return allTodos; }

    LiveData<List<Todo>> getUncompletedTodos() { return uncompletedTodos; }

    public void insert(Todo todo) { repository.insert(todo);}

    public void updateTodo(Todo todo, String data, int type) throws ParseException { repository.updateTodo(todo, data, type); }

    public void removeTodo(Todo todo) { repository.removeTodo(todo); }

}
