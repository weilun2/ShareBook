package ca.ualberta.cmput301w19t05.sharebook;

public class Book {
    private String title;
    private String auther;
    private String ISBN;
    private User owner;
    private String photo;
    private String status;

    public Book(String title, String auther, String ISBN) {
        this.title = title;
        this.auther = auther;
        this.ISBN = ISBN;

    }

    public Book(String title, String auther, String ISBN, User owner) {
        this.title = title;
        this.auther = auther;
        this.ISBN = ISBN;
        this.owner = owner;
    }

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
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

}
