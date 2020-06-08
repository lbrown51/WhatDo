package com.ad340.whatdo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Todo.class}, version = 3, exportSchema = false)
public abstract class TodoRoomDatabase extends RoomDatabase {

    public abstract TodoDao todoDao();

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
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {
                TodoDao dao = INSTANCE.todoDao();
                dao.deleteAll();
                Todo todo = new Todo(null, "First Todo", "06.10.20", null, null, false);
                dao.insert(todo);
                todo = new Todo(null, "Second Todo", "06.10.20", null, null, false);
                dao.insert(todo);
                todo = new Todo(null, "Third Todo", "06.10.20", null, null, false);
                dao.insert(todo);
                todo = new Todo(null, "Fourth Todo", "06.10.20", null, null, false);
                dao.insert(todo);
                todo = new Todo(null, "Fifth Todo", "06.10.20", null, null, false);
                dao.insert(todo);
                todo = new Todo(null, "Finished Todo", "06.10.20", null, null, true);
                dao.insert(todo);

            });


        }
    };

}
