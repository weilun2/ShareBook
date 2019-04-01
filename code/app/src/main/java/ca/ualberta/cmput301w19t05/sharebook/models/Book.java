package ca.ualberta.cmput301w19t05.sharebook.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Book
 * this class stores book information, each instance refers to a literal book owned by a user
 * different copies owned by different user are stored separately
 * <p>
 * Public Methods:
 * getters & setters
 */

public class Book implements Parcelable {

    public static final String AVAILABLE = "available";
    private String title;
    private String author;
    private String ISBN;
    private User owner;
    public static final String REQUESTED = "requested";
    public static final String ACCEPTED = "accepted";
    public static final String BORROWED = "borrowed";


    private String status;
    private String description;

    private Uri photo;//todo: multiple photos support
    private String bookId;


    public Book(String title, String author, String ISBN, User owner) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.owner = owner;
        this.bookId = UUID.randomUUID().toString();
        this.status = "available";
    }

    public Book(String title, String author, String ISBN, User owner, Uri uri) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.owner = owner;
        this.photo = uri;
        this.bookId = UUID.randomUUID().toString();
        this.status = "available";
    }

    public Book(String title, String author, String ISBN, User owner, String status, String id) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.owner = owner;
        this.status = status;
        this.bookId = id;
    }

    public Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        ISBN = in.readString();
        owner = in.readParcelable(User.class.getClassLoader());
        status = in.readString();
        description = in.readString();
        photo = Uri.parse((in.readString()));
        status = in.readString();
        bookId = in.readString();

    }

    public String getBookId() {
        return bookId;
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

    public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {


        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /*    private String title;
    private String author;
    private String ISBN;
    private User owner;
    private String status;
    private Location mLocation;
    private String description;
    private Uri photo;*/

        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(ISBN);
        dest.writeParcelable(owner, flags);
        dest.writeString(status);
        dest.writeString(description);
        dest.writeString(String.valueOf(photo));
        dest.writeString(status);
        dest.writeString(bookId);

    }
}
