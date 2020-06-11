package com.ad340.whatdo;

import androidx.annotation.Nullable;

import java.util.List;

public interface TodoHandler {
    void getTodosInRange(@Nullable TodoCalendar range);
}
