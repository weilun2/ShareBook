package ca.ualberta.cmput301w19t05.sharebook.models;

import java.util.Calendar;



/**
 *
 * A Notification contains message, date, sender, receiver, seen(boolean)
 *
 */
public class Notification {
    private String message;
    private Calendar date;
    private User sender;
    private User receiver;
    private Boolean seen;

        public Notification(String message, User sender, User receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.date = Calendar.getInstance();
        this.seen = false;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
