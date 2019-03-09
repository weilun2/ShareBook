package ca.ualberta.cmput301w19t05.sharebook;


import android.support.annotation.Nullable;

import java.io.Serializable;

public class Book implements Serializable {
    private static String title;
    private static String author;
    private String ISBN;
    private User owner;
    private String photo;
    private String status;
    private Location mLocation;
    private static String description;

    public Book(String title, String author, @Nullable String description, @Nullable String ISBN) {
        Book.title = title;
        Book.author = author;
        this.ISBN = ISBN;
        Book.description = description;

    }

    public Book(String title, String author, String ISBN, User owner) {
        Book.title = title;
        Book.author = author;
        this.ISBN = ISBN;
        this.owner = owner;
    }

    public Book() {
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public static String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Book.title = title;
    }

    public static String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        Book.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }


    public String getPhote() {
        return photo;
    }

    public void setPhote(String phote) {
        this.photo = phote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public static String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Book.description = description;
    }

    @Override
    public String toString(){
        return title + "\n" + author + "\n" + ISBN;
    }

}
