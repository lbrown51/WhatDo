package com.ad340.whatdo;

import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.ParseException;

public interface TagListener {
    void tagListSelect(String tag, int requestCode, @Nullable TextView tagText, @Nullable Todo todo) throws ParseException;
    void launchAllTags(int requestCode, @Nullable TextView newTodoTagText, @Nullable Todo todo);
}
