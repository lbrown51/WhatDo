package com.ad340.whatdo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_table")
public class Todo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Integer id;
    public Integer getId() { return id; }

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

    @Ignore
    @ColumnInfo(name = "isCompleted")
    private boolean isCompleted;
    public boolean getCompleted() { return this.isCompleted; }

    @ColumnInfo(name = "notes")
    private String notes;
    public String getNotes() { return this.notes; }

    @Ignore
    private boolean expanded;
    public boolean isExpanded() { return this.expanded; }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }

    public Todo(Integer id, @NonNull String title, String date, String time, String notes) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.isCompleted = false;
        this.expanded = false;
    }

//    @Ignore
//    public Todo(@NonNull String title) {
//        this.title = title;
//        this.expanded = false;
//    }
}
