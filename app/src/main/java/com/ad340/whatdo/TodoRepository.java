package com.ad340.whatdo;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static android.content.ContentValues.TAG;

public class TodoRepository {

    private static final String TAG = TodoRepository.class.getSimpleName();
    private TodoDao todoDao;
    private List<Todo> allTodos;
    private LiveData<List<Todo>> uncompletedTodos;
    private TodoHandler handler;

    TodoRepository(Application application, TodoHandler handler) {
        this.handler = handler;
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(application);
        todoDao = db.todoDao();
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
            allTodos = todoDao.getAllTodos();
            uncompletedTodos = todoDao.getUncompletedTodos();
        });

    }


    LiveData<List<Todo>> getUncompletedTodos() { return uncompletedTodos; }

    LiveData<List<Todo>> getTodosInRange(Calendar start, Calendar end) {
        Log.d(TAG, "getTodosInRange: " + start.getTime().toString() + " " + end.getTime().toString());
        return todoDao.getTodosInRange(start, end);
    }

    void updateTodo(Todo todo, String data, int type) throws ParseException {
        int id = todo.getId();
        switch (type) {
            case 1: // update title
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                    todoDao.updateTodoTitle(id, data);
                    handler.getTodosInRange(null);
                });
                break;
            case 2: // update date
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
                Calendar c = Calendar.getInstance();
                c.setTime(sdf.parse(data));
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                    todoDao.updateTodoDate(id, c);
                    handler.getTodosInRange(null);
                });
                break;
            case 3: // update time
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                    todoDao.updateTodoTime(id, data);
                    handler.getTodosInRange(null);
                });
                break;
            case 4: // update notes
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                    todoDao.updateTodoNotes(id, data);
                    handler.getTodosInRange(null);
                });
                break;
            case 5: // set to completed
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                    todoDao.setTodoCompleted(id);
                    handler.getTodosInRange(null);
                });
                break;
            default: // type not recognized
                StringBuilder errMessage  = new StringBuilder("Data type of ")
                        .append(type).append(" not recognized.");
                throw new IllegalArgumentException(String.valueOf(errMessage));

        }
    }

    void insert(Todo todo) {
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
            todoDao.insert(todo);
            handler.getTodosInRange(null);
        });
    }

    void removeTodo(Todo todo) {
        int id = todo.getId();
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> { todoDao.cancelTodo(id);});
    }

}
