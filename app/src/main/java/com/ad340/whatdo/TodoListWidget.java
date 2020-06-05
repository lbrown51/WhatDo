package com.ad340.whatdo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class TodoListWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_list_widget);
        setRemoteAdapter(context, views, appWidgetId);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Toast.makeText(context,"onEnabled called", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Toast.makeText(context,"onDisabled called", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        ComponentName name = new ComponentName(context, TodoListWidget.class);
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name);
        if (appWidgetIds == null || appWidgetIds.length == 0) {
            return;
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        Log.d("onReceiveTodoListWidget", intent.getAction());
        if (Objects.equals(intent.getAction(), "com.example.list.item.click")) {
            Intent launchMainIntent = new Intent(context, MainActivity.class);
            context.startActivity(launchMainIntent);
        }
    }

    private static void setRemoteAdapter(
            Context context,
            @NonNull final RemoteViews views,
            int appWidgetId
    ) {
        final Intent serviceIntent = new Intent(context, TodoListWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(R.id.todo_widget_list, serviceIntent);

        Intent todoWidgetListItemClickIntent = new Intent(context, TodoListWidget.class);
        todoWidgetListItemClickIntent.setAction("com.ad340.whatdo.widget.item.click");
        todoWidgetListItemClickIntent.setData(
                Uri.parse(todoWidgetListItemClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent todoWidgetListItemClickPendingIntent = PendingIntent.getActivity(
                        context, 0,
                        todoWidgetListItemClickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
        );
        views.setPendingIntentTemplate(R.id.todo_widget_list, todoWidgetListItemClickPendingIntent);


    }
}

