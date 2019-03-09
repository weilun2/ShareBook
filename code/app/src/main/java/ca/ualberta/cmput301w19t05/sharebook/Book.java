package ca.ualberta.cmput301w19t05.sharebook;

/**
 * Book
 *      this class stores book information, each instance refers to a literal book owned by a user
 *      different copies owned by different user are stored separately
 *
 * Public Methods:
 *      getters & setters
 */
public class Book {
    private String title;
    private String author;
    private String ISBN;
    private User owner;
    private String photo; // todo: photos set attaching not done
    private String status;
    private Location mLocation;

    public Book(String title, String author, String ISBN) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;

    }

    public Book(String title, String author, String ISBN, User owner) {
        this.title = title;
        this.author = author;
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
