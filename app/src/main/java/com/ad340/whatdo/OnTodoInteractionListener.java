package com.ad340.whatdo;

public interface OnTodoInteractionListener {
    void onSetDate(Todo todo, String date);

    void onSetTime(Todo todo, String time);
}