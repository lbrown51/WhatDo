package com.ad340.whatdo;

public class ToDoItem {
    public final String taskName;
    private boolean hasString;
    private StringBuilder dateString;


    public ToDoItem(String taskName) {

        this.taskName = taskName;
    }

    public StringBuilder getDateString() {
        return dateString;
    }

    public void setDateString(StringBuilder dateString) {
        this.dateString = dateString;
    }

    public boolean isHasString() {
        return hasString;
    }

    public void setHasString(boolean hasString) {
        this.hasString = hasString;
    }
}
