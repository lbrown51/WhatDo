package com.ad340.whatdo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoRepository {

    private TodoDao todoDao;
    private LiveData<List<Todo>> allTodos;
    private LiveData<List<Todo>> uncompletedTodos;

    TodoRepository(Application application) {
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(application);
        todoDao = db.todoDao();
        allTodos = todoDao.getAllTodos();
        uncompletedTodos = todoDao.getUncompletedTodos();
    }

    LiveData<List<Todo>> getAllTodos() { return allTodos; }

    LiveData<List<Todo>> getUncompletedTodos() { return uncompletedTodos; }

    void updateTodo(Todo todo, String data, int type) {
        int id = todo.getId();
        switch (type) {
            case Constants.TITLE: // update title
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoTitle(id, data));
                break;
            case Constants.DATE: // update date
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoDate(id, data));
                break;
            case Constants.TIME: // update time
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoTime(id, data));
                break;
            case Constants.NOTES: // update notes
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoNotes(id, data));
                break;
            case Constants.COMPLETE: // set to completed
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.setTodoCompleted(id));
                break;
            case Constants.TAG:
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoTag(id, data));
                break;
            default: // type not recognized
                StringBuilder errMessage  = new StringBuilder("Data type of ")
                        .append(type).append(" not recognized.");
                throw new IllegalArgumentException(String.valueOf(errMessage));
        }
    }

    void insert(Todo todo) {
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> { todoDao.insert(todo);});
    }

    void removeTodo(Todo todo) {
        int id = todo.getId();
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> { todoDao.cancelTodo(id);});
    }
}
