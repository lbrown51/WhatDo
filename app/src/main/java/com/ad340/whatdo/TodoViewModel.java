package com.ad340.whatdo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.text.ParseException;
import java.util.List;

public class TodoViewModel extends AndroidViewModel implements TodoHandler{

    private static final String TAG = TodoViewModel.class.getSimpleName();
    MutableLiveData<TodoCalendar> dateFilter;
    LiveData<List<Todo>> allTodos;
    TodoCalendar currentRange;
    private TodoRepository todoRepository;
    private TagRepository tagRepository;
    private LiveData<List<Tag>> allTags;
    OnTodoInteractionListener emptyListHandler;

    public TodoViewModel(Application application) {
        super(application);
        dateFilter = new MutableLiveData<>();
        todoRepository = new TodoRepository(application, this);
        tagRepository = new TagRepository(application);
        allTodos = Transformations.switchMap(dateFilter, filter -> {
            if (!filter.getShowAll())
                return todoRepository.getTodosInRange(filter.getStartDate(), filter.getEndDate(), filter.getIsCompleted());
            else
                return todoRepository.getAllTodosInRange(filter.getStartDate(), filter.getEndDate());
        });
        allTags = tagRepository.getAllTags();
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

    LiveData<List<Tag>> getAllTags() { return allTags; }

    public void insert(Todo todo) { todoRepository.insert(todo);}

    public void updateTodo(Todo todo, String data, int type) throws ParseException { todoRepository.updateTodo(todo, data, type);
        Log.d(TAG, "updateTodo: viewmodel complete");
    }

    public void removeTodo(Todo todo) { todoRepository.removeTodo(todo); }

}
