package com.ad340.whatdo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Todo.class, Tag.class}, version = 4, exportSchema = false)
public abstract class TodoRoomDatabase extends RoomDatabase {

    public abstract TodoDao todoDao();
    public abstract TagDao tagDao();

    private static volatile TodoRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    
    static TodoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoRoomDatabase.class, "todo_database")
                            .fallbackToDestructiveMigration() //
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        // When ready to persist data, uncomment onCreate and comment out onOpen
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            databaseWriteExecutor.execute(() -> {
//                TodoDao dao = INSTANCE.todoDao();
//                Todo todo = new Todo(null, "My First Todo", null, null, null, false);
//                dao.insert(todo);
//            });
//        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                TodoDao todoDao = INSTANCE.todoDao();
                TagDao tagDao = INSTANCE.tagDao();
                todoDao.deleteAll();
                tagDao.deleteAll();

                Todo todo = new Todo(null, "First Todo", null, null, null, false, null);
                todoDao.insert(todo);
                todo = new Todo(null, "Second Todo", null, null, null, false, null);
                todoDao.insert(todo);
                todo = new Todo(null, "Third Todo", null, null, null, false, null);
                todoDao.insert(todo);
                todo = new Todo(null, "Fourth Todo", null, null, null, false, null);
                todoDao.insert(todo);
                todo = new Todo(null, "Fifth Todo", null, null, null, false, null);
                todoDao.insert(todo);
                todo = new Todo(null, "Finished Todo", null, null, null, true, null);
                todoDao.insert(todo);

                Tag tag = new Tag(null, "Home");
                tagDao.insert(tag);
                tag = new Tag(null, "Work");
                tagDao.insert(tag);
                tag = new Tag(null, "Anniversary");
                tagDao.insert(tag);
            });
        }
    };

}
