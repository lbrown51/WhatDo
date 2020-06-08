package com.ad340.whatdo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TagRepository {
    private TagDao tagDao;
    private LiveData<List<Tag>> allTags;

    TagRepository(Application application) {
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(application);
        tagDao = db.tagDao();
        allTags = tagDao.getAllTags();
    }

    LiveData<List<Tag>> getAllTags() { return allTags; }

    void insert(Tag tag) {
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> { tagDao.insert(tag); });
    }

    void removeTag(Tag tag) {
        int id = tag.getId();
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> { tagDao.removeTag(id);});
    }
}
