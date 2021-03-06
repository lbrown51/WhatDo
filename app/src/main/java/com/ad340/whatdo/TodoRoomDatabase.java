package com.ad340.whatdo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Todo.class}, version = 7, exportSchema = false)
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

                todoDao.deleteAll();

                Calendar c = Calendar.getInstance();
                c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));

                Todo todo = new Todo(null, "First Todo", c, null, null, false, "", "N");
                todoDao.insert(todo);
                todo = new Todo(null, "Second Todo", c, null, null, false, "", "N");
                todoDao.insert(todo);
                todo = new Todo(null, "Third Todo", c, null, null, false, "", "N");
                todoDao.insert(todo);
                todo = new Todo(null, "Fourth Todo", c, null, null, false, "", "N");
                todoDao.insert(todo);
                todo = new Todo(null, "Fifth Todo", c, null, null, false, "", "N");
                todoDao.insert(todo);
                todo = new Todo(null, "Finished Todo", c, null, null, true, "", "N");
                todoDao.insert(todo);

                Log.d(TAG, "onOpen: dummys made");
            });
        }
    };

}
