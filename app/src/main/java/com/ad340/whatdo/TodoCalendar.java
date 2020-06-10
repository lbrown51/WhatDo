package com.ad340.whatdo;

import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Observable;

public class TodoCalendar extends Observable {
    private Calendar startDate;
    private Calendar endDate;
    private PropertyChangeListener listener;

    public TodoCalendar(Calendar start, Calendar end) {
        startDate = start;
        endDate = end;
    }

    // GETTERS

    public Calendar getStartDate() { return startDate; }
    public Calendar getEndDate() { return endDate; }
    //public PropertyChangeListener getListener() { return listener; }

    // SETTERS

    public void setListener(PropertyChangeListener newListener) { listener = newListener; }

    public void incrementDate(int pos, int dayCount) {
        if (pos == 1) {
            startDate.add(Calendar.DATE, dayCount);
        } else if (pos == 2) {
            endDate.add(Calendar.DATE, dayCount);
        }
        notifyObservers();
    }

    public void setDateRange(Calendar start, Calendar end) {
        startDate = start;
        endDate = end;
        notifyObservers();
    }

}
