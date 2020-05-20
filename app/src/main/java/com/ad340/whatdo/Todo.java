package com.ad340.whatdo;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_table")
public class Todo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    public String getTitle() { return this.title; }

    @ColumnInfo(name = "date")
    private String date;
    public String getDate() { return this.date; }

    @ColumnInfo(name = "time")
    private String time;
    public String getTime() { return this.time; }

    @ColumnInfo(name = "isCompleted")
    private boolean isCompleted;
    public boolean getCompleted() { return this.isCompleted; }

    @ColumnInfo(name = "notes")
    private String notes;
    public String getNotes() { return this.notes; }

    public Todo(@NonNull String title, String date, String time, String notes) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.isCompleted = false;
    }

    public Todo(@NonNull String title) {
        this(title, null, null, null);
    }
}
