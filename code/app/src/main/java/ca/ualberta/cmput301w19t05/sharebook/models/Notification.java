package ca.ualberta.cmput301w19t05.sharebook.models;

/**
 *
 * A Notification contains message, date, sender, receiver, seen(boolean)
 *
 */
public class Notification {
    public String body;
    public String title;

    public Notification(String body, String title) {
        this.body = body;
        this.title = title;
    }
}
