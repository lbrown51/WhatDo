package com.ad340.whatdo;

import android.app.Application;
import android.content.Context;

import androidx.core.util.Consumer;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository repository;
    private LiveData<List<Todo>> allTodos;
    private LiveData<List<Todo>> uncompletedTodos;

    public TodoViewModel(Application application) {
        super(application);
        repository = new TodoRepository(application);
        allTodos = repository.getAllTodos();
        uncompletedTodos = repository.getUncompletedTodos();
    }

    LiveData<List<Todo>> getAllTodos() { return allTodos; }

    LiveData<List<Todo>> getTodosInRange(Calendar start, Calendar end) {
        return repository.getTodosInRange(start, end);
    }

    //LiveData<List<Todo>> getAllTodos(Consumer<List<Todo>> responseCallback) { return allTodos; }

    LiveData<List<Todo>> getUncompletedTodos() { return uncompletedTodos; }

    public void insert(Todo todo) { repository.insert(todo);}

    public void updateTodo(Todo todo, String data, int type) throws ParseException { repository.updateTodo(todo, data, type); }

    public void removeTodo(Todo todo) { repository.removeTodo(todo); }
}
