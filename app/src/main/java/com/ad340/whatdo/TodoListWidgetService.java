package com.ad340.whatdo;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TodoListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodoListWidgetDataProvider(this, intent);
    }
}
