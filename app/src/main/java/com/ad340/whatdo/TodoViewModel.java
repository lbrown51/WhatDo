package com.ad340.whatdo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TodoViewModel extends AndroidViewModel implements TodoHandler{

    private static final String TAG = TodoViewModel.class.getSimpleName();
    MutableLiveData<TodoCalendar> dateFilter;
    LiveData<List<Todo>> allTodos;
    TodoCalendar currentRange;
    private TodoRepository todoRepository;
    OnTodoInteractionListener emptyListHandler;
    private ArrayList<String> tags;

    public TodoViewModel(Application application) {
        super(application);
        dateFilter = new MutableLiveData<>();
        todoRepository = new TodoRepository(application, this);
        allTodos = Transformations.switchMap(dateFilter, filter -> {
            if (!filter.getShowAll())
                {Log.d(TAG, "TodoViewModel: " + filter.getTag());
                return todoRepository.getTodosInRange(filter.getStartDate(), filter.getEndDate(), filter.getIsCompleted(), filter.getTag());}
            else
                return todoRepository.getAllTodosInRange(filter.getStartDate(), filter.getEndDate());
        });
        loadTags();
    }

    LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }

    @Override
    public void getTodosInRange(@Nullable TodoCalendar range) {
        if (range != null) {
            currentRange = range;
        }
        dateFilter.postValue(currentRange);
    }

    @Override
    public void setEmptyList() {
        emptyListHandler.emptyList();
    }

    public void insert(Todo todo) { todoRepository.insert(todo);}

    public void updateTodo(Todo todo, String data, int type) throws ParseException {
        todoRepository.updateTodo(todo, data, type);
        Log.d(TAG, "updateTodo: viewmodel complete");
    }

    public void removeTodo(Todo todo) { todoRepository.removeTodo(todo); }

    public void saveTags() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tags);
        editor.putString("tags list", json);
        editor.apply();
    }

    private void loadTags() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("tags list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        tags = gson.fromJson(json, type);

        if (tags == null) {
            tags = new ArrayList<>();
        }
    }

    public void addTag(String tag) {
        tags.add(0, tag);
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void updateTag(String tag) {
        tags.add(0, tags.remove(tags.indexOf(tag)));
    }


}
