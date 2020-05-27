package com.ad340.whatdo;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository repository;
    private LiveData<List<Todo>> allTodos;

    public TodoViewModel(Application application) {
        super(application);
        repository = new TodoRepository(application);
        allTodos = repository.getAllTodos();
    }

    LiveData<List<Todo>> getAllTodos() { return allTodos; }

    public void insert(Todo todo) { repository.insert(todo);}

    public void updateTodo(Todo todo, String data, int type) { repository.updateTodo(todo, data, type); }
}
