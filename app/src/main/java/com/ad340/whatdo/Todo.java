package com.ad340.whatdo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
    private Date date;
    public Date getDate() { return this.date; }

    @ColumnInfo(name = "time")
    private String time;
    public String getTime() { return this.time; }

    @ColumnInfo(name = "isCompleted")
    private boolean isCompleted;
    public boolean getCompleted() { return this.isCompleted; }

    @ColumnInfo(name = "notes")
    private String notes;
    public String getNotes() { return this.notes; }

    public Todo(Integer id, @NonNull String title, Date date, String time, String notes, Boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.isCompleted = isCompleted;
    }

//    @Ignore
//    public Todo(@NonNull String title) {
//        this.title = title;
//    }
}
