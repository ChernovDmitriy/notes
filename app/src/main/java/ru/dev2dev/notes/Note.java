package ru.dev2dev.notes;

import java.io.Serializable;

/**
 * Created by Dmitriy on 21.04.2016.
 */
public class Note implements Serializable {
    private long id;
    private String title;
    private String description;
    private String imagePath;
    private String date;

    public Note() {}

    public Note(long id, String title, String description, String imagePath, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "note: id = "+this.getId()+", name = "+this.title;
    }

}
