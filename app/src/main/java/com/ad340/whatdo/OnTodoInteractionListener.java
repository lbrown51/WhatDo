package com.ad340.whatdo;

import java.text.ParseException;

public interface OnTodoInteractionListener {
    void onUpdateTodo(Todo todo, String data, int type) throws ParseException;
    void emptyList();
}