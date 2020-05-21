package com.ad340.whatdo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert
    void insert(Todo todo);

    @Query("SELECT * from todo_table ORDER BY title ASC")
    LiveData<List<Todo>> getAllTodos();

    @Query("DELETE FROM todo_table")
    void deleteAll();
}
