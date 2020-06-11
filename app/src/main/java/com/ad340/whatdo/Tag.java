package com.ad340.whatdo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag_table")
public class Tag {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Integer id;
    public Integer getId() { return id; }

    @NonNull
    @ColumnInfo(name = "name")
    private String name;
    public String getName() { return this.name; }

    public Tag(Integer id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }
}
