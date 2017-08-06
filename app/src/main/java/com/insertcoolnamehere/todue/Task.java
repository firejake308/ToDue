package com.insertcoolnamehere.todue;

import java.util.Date;

/**
 * Represents an individual task in a to-do list
 */

public class Task {
    private String title;
    private Date doDate;
    private Date dueDate;
    private String category;

    public Task(String title, Date doDate, Date dueDate,String category) {
        this.title = title;
        this.doDate = doDate;
        this.dueDate = dueDate;
        this.category = category;
    }

    public Date getDoDate() {
        return doDate;
    }

    public void setDoDate(Date doDate) {
        this.doDate = doDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
