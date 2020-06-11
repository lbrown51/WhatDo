package com.ad340.whatdo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

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
    private Calendar date;
    public Calendar getDate() { return this.date; }

    @ColumnInfo(name = "time")
    private String time;
    public String getTime() { return this.time; }

    @ColumnInfo(name = "isCompleted")
    private boolean isCompleted;
    public boolean getCompleted() { return this.isCompleted; }

    @ColumnInfo(name = "notes")
    private String notes;
    public String getNotes() { return this.notes; }

    @ColumnInfo(name = "tag")
    private String tag;
    public String getTag() { return this.tag; }

    public Todo(Integer id, @NonNull String title, Calendar date, String time, String notes, Boolean isCompleted, String tag) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.isCompleted = isCompleted;
        this.tag = tag;
    }
}
