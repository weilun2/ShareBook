package ca.ualberta.cmput301w19t05.sharebook.tools;

import ca.ualberta.cmput301w19t05.sharebook.models.Data;
import ca.ualberta.cmput301w19t05.sharebook.models.Notification;

public class Sender {

    public Notification notification;
    public Data data;
    public String to;

    public Sender() {
    }

    public Sender(Notification notification, Data data, String to) {
        this.notification = notification;
        this.data = data;
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
