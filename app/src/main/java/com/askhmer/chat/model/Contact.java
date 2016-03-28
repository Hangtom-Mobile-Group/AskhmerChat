package com.askhmer.chat.model;

/**
 * Created by Lincoln on 15/01/16.
 */
public class Contact {
    private String title, genre;

    public Contact() {
    }

    public Contact(String title, String genre) {
        this.title = title;
        this.genre = genre;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
