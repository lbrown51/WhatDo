package com.ad340.whatdo;

import androidx.annotation.Nullable;

public interface TodoHandler {
    void getTodosInRange(@Nullable TodoCalendar range);
    void setEmptyList();
}
