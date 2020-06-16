package com.ad340.whatdo;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TodoRepository {

    private static final String TAG = TodoRepository.class.getSimpleName();
    private TodoDao todoDao;
    private TodoHandler handler;

    TodoRepository(Application application, TodoHandler handler) {
        this.handler = handler;
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(application);
        todoDao = db.todoDao();
    }

    LiveData<List<Todo>> getTodosInRange(Calendar start, Calendar end, boolean isCompleted, String tag) {
        Log.d(TAG, "getTodosInRange: " + start.getTime().toString() + " " + end.getTime().toString());
        LiveData<List<Todo>> temp = todoDao.getTodosInRange(start, end, isCompleted, tag);
        if (temp.getValue() == null)
            handler.setEmptyList();
        return temp;
    }

    LiveData<List<Todo>> getAllTodosInRange(Calendar start, Calendar end) {
        Log.d(TAG, "getTodosInRange: " + start.getTime().toString() + " " + end.getTime().toString());
        return todoDao.getAllTodosInRange(start, end);
    }

    void updateTodo(Todo todo, String data, int type) throws ParseException {
        int id = todo.getId();
        switch (type) {
            case Constants.TITLE: // update title
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                    todoDao.updateTodoTitle(id, data);
                    handler.getTodosInRange(null);
                });
                break;
            case Constants.DATE: // update date
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
                Calendar c = Calendar.getInstance();
                c.setTime(sdf.parse(data));
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                    todoDao.updateTodoDate(id, c);
                    handler.getTodosInRange(null);
                });
                break;
            case Constants.TIME: // update time
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                    todoDao.updateTodoTime(id, data);
                    handler.getTodosInRange(null);
                });
                break;
            case Constants.NOTES: // update notes
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                    todoDao.updateTodoNotes(id, data);
                    handler.getTodosInRange(null);
                });
                break;
            case Constants.COMPLETE: // set to completed
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                    todoDao.setTodoCompleted(id);
                    handler.getTodosInRange(null);
                });
                break;
            case Constants.TAG:
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
                        todoDao.updateTodoTag(id, data);
                        handler.getTodosInRange(null);
                });
                break;
            case Constants.RECUR:
                TodoRoomDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodoRecur(id, data));
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
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
            todoDao.cancelTodo(id);
            handler.getTodosInRange(null);
        });
    }
}
