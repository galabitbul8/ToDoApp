package com.galab_rotemle.ex3;

public class TodoInfo {
    private String title;
    private String description;
    private String date;
    private String time;

    public TodoInfo(String title, String description, int datetime)
    {
        this.title = title;
        this.description = description;
        // TODO: change this - need to get the datetime integer and transform it into strings (Calender)
        this.date = "11/02/1993";
        this.time ="14:50";
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
