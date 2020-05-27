package com.ad340.whatdo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert
    void insert(Todo todo);

    @Query("SELECT * from todo_table ORDER BY title ASC")
    LiveData<List<Todo>> getAllTodos();

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Query("UPDATE todo_table SET title = :title WHERE id = :id")
    void updateTodoTitle(int id, String title);

    @Query("UPDATE todo_table SET date = :date WHERE id = :id")
    void updateTodoDate(int id, String date);

    @Query("UPDATE todo_table SET time = :time WHERE id = :id")
    void updateTodoTime(int id, String time);

    @Query("UPDATE todo_table SET notes = :notes WHERE id = :id")
    void updateTodoNotes(int id, String notes);
}
