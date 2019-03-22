package ca.ualberta.cmput301w19t05.sharebook.models;

import java.util.UUID;

/**
 * each action made by user will generate a record which record book, owner, borrower
 */
public class Record {
    private String bookId;
    private User owner;
    private String recordId;
    private User borrower;
    private String status;


    public Record(Book book, User borrower) {
        this.recordId = UUID.randomUUID().toString();
        this.bookId = book.getBookId();
        this.owner = book.getOwner();
        this.borrower = borrower;
        this.status = "requested";
    }



    public Record() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
