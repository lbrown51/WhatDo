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

    void updateTodo(Todo todo, String data, int type) {
        int id = todo.getId();
        switch (type) {
            case 1: // update title
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoTitle(id, data));
                break;
            case 2: // update date
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoDate(id, data));
                break;
            case 3: // update time
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoTime(id, data));
                break;
            case 4: // update notes
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoNotes(id, data));
            default: // type not recognized
                StringBuilder errMessage  = new StringBuilder("Data type of ")
                        .append(type).append(" not recognized.");
                throw new IllegalArgumentException(String.valueOf(errMessage));

        }
    }

    void insert(Todo todo) {
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> { todoDao.insert(todo);});
    }

    void updateTodos(Todo... todos) {
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodos(todos));
    };
}
