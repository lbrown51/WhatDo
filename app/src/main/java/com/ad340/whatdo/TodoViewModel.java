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
    MutableLiveData<TodoCalendar> dateFilter;
    LiveData<List<Todo>> allTodos;
    TodoCalendar currentRange;
    private TodoRepository todoRepository;
    private TagRepository tagRepository;
    private LiveData<List<Todo>> allTodos;
    private LiveData<List<Todo>> uncompletedTodos;
    private LiveData<List<Tag>> allTags;

    public TodoViewModel(Application application) {
        super(application);
        dateFilter = new MutableLiveData<>();
        todoRepository = new TodoRepository(application, this);
        tagRepository = new TagRepository(application);
        allTodos = Transformations.switchMap(dateFilter, filter -> todoRepository.getTodosInRange(filter.getStartDate(), filter.getEndDate()));
        allTags = tagRepository.getAllTags();
        uncompletedTodos = todoRepository.getUncompletedTodos();
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

    LiveData<List<Tag>> getAllTags() { return allTags; }

    LiveData<List<Todo>> getUncompletedTodos() { return uncompletedTodos; }

    public void insert(Todo todo) { todoRepository.insert(todo);}

    public void updateTodo(Todo todo, String data, int type) throws ParseException { todoRepository.updateTodo(todo, data, type); }

    public void removeTodo(Todo todo) { todoRepository.removeTodo(todo); }

}
