package ca.ualberta.cmput301w19t05.sharebook;

import org.junit.Test;

import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.Location;
import ca.ualberta.cmput301w19t05.sharebook.models.Notification;
import ca.ualberta.cmput301w19t05.sharebook.models.User;

import static org.junit.Assert.assertTrue;

public class UserTest {
    private Book mBook;
    private Location mLocation;
    private User owner;
    private User borrower;

    public UserTest(){
        mLocation = new Location(12,13,"edmonton", "123ASD", "bedroom");
        owner = new User("gsz", "gg", "gg@gg.com");
        borrower = new User("lzz", "zz", "zz@zz.com");
        mBook = new Book("art of poop", "hwl", "fakeISBN", owner);
        mBook.setStatus("AVAILABLE");
    }

    @Test
    public void sendMessageTest(){
        Notification mNotification = owner.sendMessage("Testing message: 1.", borrower);
        assertTrue(mNotification.getMessage().equals("Testing message: 1."));
        assertTrue(mNotification.getReceiver().equals(borrower));
        assertTrue(mNotification.getSender().equals(owner));
        assertTrue(mNotification.getSeen().equals(false));
    }

    @Test
    public void sendRequestTest(){
        String ori_status = mBook.getStatus();
        borrower.sendRequest(mBook);
        assertTrue(ori_status != mBook.getStatus());
    }

    @Test
    public void acceptTest(){
        String ori_status = mBook.getStatus();
        assertTrue(ori_status.equals("REQUESTED"));
        owner.accept(mBook, mLocation);
        assertTrue(mBook.getStatus().equals("ACCEPTED"));
    }

    @Test
    public void declineTest(){
        String ori_status = mBook.getStatus();
        borrower.sendRequest(mBook);
        assertTrue(ori_status != mBook.getStatus());
    }
}
