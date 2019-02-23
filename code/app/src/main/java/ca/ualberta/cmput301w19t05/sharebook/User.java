package ca.ualberta.cmput301w19t05.sharebook;

import java.util.ArrayList;

public class User {
    private String userID;
    private String username;
    private String email;
    private ArrayList<Book> myBooks;


    public User(String userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<Book> getShelf() {
        return myBooks;
    }

    public void setShelf(ArrayList<Book> shelf) {
        this.myBooks = shelf;
    }

    public void accept(Book book, Location mLocation){
        book.setStatus("ACCEPTED");
        sendMessage("accept", book.getOwner());
    }
    public void decline(Book book){
        sendMessage("decline", book.getOwner());
    }
    public void sendRequest(Book book){
        book.setStatus("REQUESTED");
        sendMessage("Request", book.getOwner());
        Record record = new Record(book,this,book.getOwner());
    }
    public Notification sendMessage( String message, User receiver){
        Notification notification = new Notification(message,this, receiver);
        return notification;
    }



}
