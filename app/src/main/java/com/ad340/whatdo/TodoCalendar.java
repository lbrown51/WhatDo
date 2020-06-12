package com.ad340.whatdo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Observable;

public class TodoCalendar extends Observable {
    private Calendar startDate;
    private Calendar endDate;
    private boolean isCompleted;
    private boolean showAll;
    protected PropertyChangeSupport propertyChangeSupport;
    public TodoCalendar(Calendar start, Calendar end) {
        propertyChangeSupport = new PropertyChangeSupport(this);
        startDate = start;
        endDate = end;
        isCompleted = false;
        showAll = false;
    }

    // GETTERS

    public Calendar getStartDate() { return startDate; }
    public Calendar getEndDate() { return endDate; }
    public boolean getIsCompleted() { return isCompleted; }
    public boolean getShowAll() { return showAll; }

    // SETTERS

    public void setListener(PropertyChangeListener newListener) {
        propertyChangeSupport.addPropertyChangeListener(newListener);
    }

    public void incrementDate(int pos, int dayCount) {
        if (pos == 1) {
            startDate.add(Calendar.DATE, dayCount);
        } else if (pos == 2) {
            endDate.add(Calendar.DATE, dayCount);
        }
        notifyObservers();
    }

    public void setIsCompleted(boolean setCompleted) {
        isCompleted = setCompleted;
        notifyObservers();
    }

    public void setShowAll(boolean setShowAll) {
        showAll = setShowAll;
        notifyObservers();
    }

    public void setDateRange(Calendar start, Calendar end) {
        startDate = start;
        endDate = end;
        notifyObservers();
        propertyChangeSupport.firePropertyChange("Start & End Dates", startDate, endDate);
    }

}
