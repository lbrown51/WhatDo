package com.ad340.whatdo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.R.id.text1;
import static android.R.layout.simple_list_item_1;

public class TodoListWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List<String> todoList;
    Context mContext = null;
    private TodoDao todoDao;


    public TodoListWidgetDataProvider(Context context, Intent intent) {
        mContext = context;
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(context);
        todoDao = db.todoDao();
        todoList = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        try {
            initData();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDataSetChanged() {
        try {
            initData();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initData() throws ExecutionException, InterruptedException {

        todoList.clear();
        todoList = new getStaticUncompletedTodoTitlesAsyncTask(todoDao).execute().get();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                simple_list_item_1);

        view.setTextViewText(text1, todoList.get(position));
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private static class getStaticUncompletedTodoTitlesAsyncTask
            extends AsyncTask<Void, Void, List<String>> {

        private TodoDao mAsyncTodoDao;

        getStaticUncompletedTodoTitlesAsyncTask(TodoDao dao) {
            mAsyncTodoDao = dao;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return mAsyncTodoDao.getStaticUncompletedTodoTitles();
        }
    }
}


