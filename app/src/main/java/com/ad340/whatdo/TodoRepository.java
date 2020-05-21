package com.ad340.whatdo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoRepository {

    private TodoDao todoDao;
    private LiveData<List<Todo>> allTodos;

    TodoRepository(Application application) {
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(application);
        todoDao = db.todoDao();
        allTodos = todoDao.getAllTodos();
    }

    LiveData<List<Todo>> getAllTodos() { return allTodos; }

    void insert(Todo todo) {
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> { todoDao.insert(todo);});
    }
}
