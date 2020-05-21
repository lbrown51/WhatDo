package com.ad340.whatdo;

public class ToDoItem {
    public final String taskName;
    private boolean expanded;
    private boolean hasString;
    private StringBuilder dateString;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public ToDoItem(String taskName) {

        this.taskName = taskName;
        this.expanded = false;
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
