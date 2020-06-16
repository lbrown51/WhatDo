package com.ad340.whatdo;

import android.util.Log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Observable;

import static android.content.ContentValues.TAG;

public class TodoCalendar extends Observable {
    private Calendar startDate;
    private Calendar endDate;
    private String tag;
    private boolean isCompleted;
    private boolean showAll;
    private static final String TAG = TodoCalendar.class.getSimpleName();
    protected PropertyChangeSupport propertyChangeSupport;
    public TodoCalendar(Calendar start, Calendar end, String tagFilter) {
        propertyChangeSupport = new PropertyChangeSupport(this);
        startDate = start;
        endDate = end;
        isCompleted = false;
        showAll = false;
        tag = tagFilter;
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

//    public void incrementDate(int pos, int dayCount) {
//        if (pos == 1) {
//            startDate.add(Calendar.DATE, dayCount);
//        } else if (pos == 2) {
//            endDate.add(Calendar.DATE, dayCount);
//        }
//        notifyObservers();
//    }

//    public void setIsCompleted(boolean setCompleted) {
//        isCompleted = setCompleted;
//        notifyObservers();
//    }
//
//    public void setShowAll(boolean setShowAll) {
//        showAll = setShowAll;
//        notifyObservers();
//    }

    public void setTag(String tag) {
        Log.d(TAG, "setTag: " + tag);
        this.tag = tag;
        updateDateRangeWithTag(startDate, endDate, this.tag);
    }

    public String getTag() { return tag; }

    public void setDateRange(Calendar start, Calendar end) {
        startDate = start;
        endDate = end;
        tag = "%";
        notifyObservers();
        propertyChangeSupport.firePropertyChange("Start & End Dates", startDate, endDate);
    }

    private void updateDateRangeWithTag(Calendar start, Calendar end, String tag) {
        startDate = start;
        endDate = end;
        this.tag = tag;
        notifyObservers();
        propertyChangeSupport.firePropertyChange("Start & End Dates", startDate, endDate);
    }

}
