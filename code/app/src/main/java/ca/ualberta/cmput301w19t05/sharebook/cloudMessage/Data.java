package ca.ualberta.cmput301w19t05.sharebook.cloudMessage;

public class Data {
    public String bookId;
    public String requestType;

    public Data(String bookId, String requestType) {
        this.bookId = bookId;
        this.requestType = requestType;
    }
}
