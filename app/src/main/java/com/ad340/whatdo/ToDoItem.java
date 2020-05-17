package com.ad340.whatdo;

public class ToDoItem {
    public final String taskName;
    private boolean expanded;

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
}
