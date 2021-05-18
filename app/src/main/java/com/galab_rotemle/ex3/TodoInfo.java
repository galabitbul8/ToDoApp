package com.galab_rotemle.ex3;

import java.text.SimpleDateFormat;

public class TodoInfo {
    private int id;
    private String title;
    private String description;
    private String date;
    private String time;

    public TodoInfo(int id,String title, String description, long datetime)
    {
        this.title = title;
        this.description = description;
        this.date = new SimpleDateFormat("dd/MM/yyyy").format(datetime);
        this.time =new SimpleDateFormat("HH:mm").format(datetime);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
