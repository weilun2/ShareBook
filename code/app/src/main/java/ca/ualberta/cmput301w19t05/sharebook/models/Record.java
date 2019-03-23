package ca.ualberta.cmput301w19t05.sharebook.models;

/**
 * each action made by user will generate a record which record book, owner, borrower
 */
public class Record {

    private String ownerName;
    private String bookName;
    private String borrowerName;
    private String status;
    private boolean seen;

    public Record(String ownerName, String bookName, String borrowerName, String status, boolean seen) {
        this.ownerName = ownerName;
        this.bookName = bookName;
        this.borrowerName = borrowerName;
        this.status = status;
        this.seen = seen;
    }

    public Record() {
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
