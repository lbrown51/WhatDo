package com.ad340.whatdo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    LiveData<List<Todo>> getTodosInRange(Date start, Date end) { return todoDao.getTodosInRange(start, end); }

    void updateTodo(Todo todo, String data, int type) throws ParseException {
        int id = todo.getId();
        switch (type) {
            case 1: // update title
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoTitle(id, data));
                break;
            case 2: // update date
                SimpleDateFormat sdf = new SimpleDateFormat("DD.mm.YY");
                Date date = sdf.parse(data);
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoDate(id, date));
                break;
            case 3: // update time
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoTime(id, data));
                break;
            case 4: // update notes
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoNotes(id, data));
                break;
            case 5: // set to completed
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.setTodoCompleted(id));
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
