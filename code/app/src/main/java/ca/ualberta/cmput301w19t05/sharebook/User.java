package ca.ualberta.cmput301w19t05.sharebook;

import android.content.Intent;

import java.net.URI;
import java.util.ArrayList;

public class User {
    private String userID;
    private String username;
    private String email;
    private URI image;
    private ArrayList<Book> myBooks;


    public User(String userID, String username, String email, URI image) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.image = image;
    }

    public URI getImage(){ return image; }

    public void setUserimage(URI image) { this.image = image; }

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


    
    public void  addShelf(Intent data){
        Book book = (Book) data.getBundleExtra("B").getSerializable("getB");
        myBooks.add(book);
    }

    public void editShelf(Intent data){

    }

}
