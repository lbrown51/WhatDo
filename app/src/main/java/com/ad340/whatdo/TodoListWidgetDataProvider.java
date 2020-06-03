package com.ad340.whatdo;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.text1;
import static android.R.layout.simple_list_item_1;

public class TodoListWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List<String> todoListView = new ArrayList<>();
    Context mContext = null;

    public TodoListWidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
    }


    @Override
    public void onDataSetChanged() {
        initData();
    }

    private void initData() {
        todoListView.clear();
        for (int i = 1; i <= 15; i++) {
            todoListView.add("ListView item " + i);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return todoListView.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                simple_list_item_1);

        view.setTextViewText(text1, todoListView.get(position));
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
}
