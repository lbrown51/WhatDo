package com.ad340.whatdo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository todoRepository;
    private TagRepository tagRepository;
    private LiveData<List<Todo>> allTodos;
    private LiveData<List<Todo>> uncompletedTodos;
    private LiveData<List<Tag>> allTags;

    public TodoViewModel(Application application) {
        super(application);
        todoRepository = new TodoRepository(application);
        tagRepository = new TagRepository(application);
        allTodos = todoRepository.getAllTodos();
        uncompletedTodos = todoRepository.getUncompletedTodos();
        allTags = tagRepository.getAllTags();
    }

    LiveData<List<Todo>> getAllTodos() { return allTodos; }

    LiveData<List<Tag>> getAllTags() { return allTags; }

    LiveData<List<Todo>> getUncompletedTodos() { return uncompletedTodos; }

    public void insertTodo(Todo todo) { todoRepository.insert(todo);}

    public void updateTodo(Todo todo, String data, int type) { todoRepository.updateTodo(todo, data, type); }

    public void removeTodo(Todo todo) { todoRepository.removeTodo(todo); }
}
