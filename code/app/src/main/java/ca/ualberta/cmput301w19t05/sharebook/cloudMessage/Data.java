package ca.ualberta.cmput301w19t05.sharebook.cloudMessage;



public class Data {
    public String bookId;
    public String requestType;
    public String sender;
    public String receiver;

    public Data(String bookId, String requestType, String sender, String receiver) {
        this.bookId = bookId;
        this.requestType = requestType;
        this.sender = sender;
        this.receiver = receiver;
    }
}
