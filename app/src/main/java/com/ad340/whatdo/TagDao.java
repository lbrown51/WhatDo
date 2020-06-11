package com.ad340.whatdo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insert(Tag tag);

    @Query("SELECT * from tag_table ORDER BY name ASC")
    LiveData<List<Tag>> getAllTags();

    @Query("DELETE FROM tag_table")
    void deleteAll();

    @Query("DELETE FROM tag_table WHERE id = :id")
    void removeTag(int id);
}
