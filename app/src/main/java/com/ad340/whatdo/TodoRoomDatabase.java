package com.ad340.whatdo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;

@Database(entities = {Todo.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TodoRoomDatabase extends RoomDatabase {

    public abstract TodoDao todoDao();

    private static final String TAG = TodoRoomDatabase.class.getSimpleName();
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
                Calendar c = Calendar.getInstance();
                c.set(2020, 5, 10);

                Todo todo = new Todo(null, "First Todo", c, null, null, false);
                dao.insert(todo);
                todo = new Todo(null, "Second Todo", c, null, null, false);
                dao.insert(todo);
                todo = new Todo(null, "Third Todo", c, null, null, false);
                dao.insert(todo);
                todo = new Todo(null, "Fourth Todo", c, null, null, false);
                dao.insert(todo);
                todo = new Todo(null, "Fifth Todo", c, null, null, false);
                dao.insert(todo);
                todo = new Todo(null, "Finished Todo", c, null, null, true);
                dao.insert(todo);
                Log.d(TAG, "onOpen: dummy todos added");
            });


        }
    };

}
