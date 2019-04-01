package ca.ualberta.cmput301w19t05.sharebook.models;

/**
 * Data
 * this class stores data information, each instance refers to a data which offers a share book record, incude
 * bookID, requestTyoe, sender, receiver, book name and sender name.
 * <p>
 * Public Methods:
 * getters & setters
 */


public class Data {
    public String bookId;
    public String requestType;
    public String sender;
    public String receiver;
    public String bookName;
    public String senderName;

    public Data(String bookId, String requestType, String sender, String receiver) {
        this.bookId = bookId;
        this.requestType = requestType;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Data(String bookId, String requestType, String sender, String receiver, String bookName, String senderName) {
        this.bookId = bookId;
        this.requestType = requestType;
        this.sender = sender;
        this.receiver = receiver;
        this.bookName = bookName;
        this.senderName = senderName;
    }

    public Data() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
