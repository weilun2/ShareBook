package ca.ualberta.cmput301w19t05.sharebook;


import android.net.Uri;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String ISBN;
    private User owner;
    private String status;
    private Location mLocation;
    private String description;
    private Uri photo;

    public Book(String title, String author, String ISBN, User owner) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.owner = owner;
    }

    public Book(String title, String author, String ISBN, User owner, Uri uri) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.owner = owner;
        this.photo = uri;
    }

    public Book() {

    }


    public String getPhoto() {
        return photo.toString();
    }

    public void setPhoto(String photo) {
        this.photo = Uri.parse(photo);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return title + "\n" + author + "\n" + ISBN;
    }

}
