package com.ad340.whatdo;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityUnitTests {

    @Test
    public void get_completedWorks() {
        Todo todo = new Todo(null, "Test todo", null, null, null, false, null, "N");
        assertFalse(todo.getCompleted());
    }
}