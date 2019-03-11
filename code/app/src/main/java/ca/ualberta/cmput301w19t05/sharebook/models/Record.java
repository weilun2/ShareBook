package ca.ualberta.cmput301w19t05.sharebook.models;

/**
 * each action made by user will generate a record which record book, owner, borrower
 */
public class Record {
    private Book book;
    private User owner;
    private User borrower;


    public Record(Book book, User owner, User borrower) {
        this.book = book;
        this.owner = owner;
        this.borrower = borrower;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }
}
