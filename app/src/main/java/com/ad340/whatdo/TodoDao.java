package com.ad340.whatdo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Calendar;
import java.util.List;

@Dao
public interface TodoDao {

    @Insert
    void insert(Todo todo);

    @Query("SELECT * from todo_table ORDER BY title ASC")
    List<Todo> getAllTodos();

    @Query("SELECT * FROM todo_table WHERE NOT isCompleted")
    LiveData<List<Todo>> getUncompletedTodos();

    @Query("SELECT title FROM todo_table WHERE NOT isCompleted")
    List<String> getStaticUncompletedTodoTitles();

    @Query("SELECT * FROM todo_table WHERE date BETWEEN :start AND :end AND isCompleted = :completed AND tag LIKE :tag")
    LiveData<List<Todo>> getTodosInRange(Calendar start, Calendar end, boolean completed, String tag);

    @Query("SELECT * FROM todo_table WHERE date BETWEEN :start AND :end")
    LiveData<List<Todo>> getAllTodosInRange(Calendar start, Calendar end);

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Query("DELETE FROM todo_table WHERE id = :id")
    void cancelTodo(int id);

    @Query("UPDATE todo_table SET title = :title WHERE id = :id")
    void updateTodoTitle(int id, String title);

    @Query("UPDATE todo_table SET date = :date WHERE id = :id")
    void updateTodoDate(int id, Calendar date);

    @Query("UPDATE todo_table SET time = :time WHERE id = :id")
    void updateTodoTime(int id, String time);

    @Query("UPDATE todo_table SET notes = :notes WHERE id = :id")
    void updateTodoNotes(int id, String notes);

    @Query("UPDATE todo_table SET tag = :tag WHERE id = :id")
    void updateTodoTag(int id, String tag);

    @Query("UPDATE todo_table SET tag = null WHERE id = :id")
    void removeTodoTag(int id);

    @Query("UPDATE todo_table SET isCompleted = 1 WHERE id = :id")
    void setTodoCompleted(int id);

    @Query("UPDATE todo_table SET recurrence = :recurrence WHERE id = :id ")
    void updateTodoRecur(int id, String recurrence);
}
