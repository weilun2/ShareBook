package ca.ualberta.cmput301w19t05.sharebook;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;

import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.Location;
import ca.ualberta.cmput301w19t05.sharebook.models.Notification;
import ca.ualberta.cmput301w19t05.sharebook.models.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserTest {
    private User TestUser;
    private Uri TestImage;
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
    @Before
    public void setup(){
        TestUser = new User("TestID","TestUserName","Test@email.com",TestImage);
    }
    @Test
    public void testConstructor(){
      assertEquals("TestID",TestUser.getUserID());
      assertEquals("TestUserName",TestUser.getUsername());
      assertEquals("Test@email.com",TestUser.getEmail());
      assertEquals(TestImage,TestUser.getImage());
    }
    @Test
    public void testSetUserID(){
        TestUser.setUserID("TestID");
        assertEquals("TestID",TestUser.getUserID());
    }
    @Test
    public void testSetUserName(){
        TestUser.setUsername("TestUserName");
        assertEquals("TestUserName",TestUser.getUsername());
    }
    @Test
    public void testSetEmail(){
        TestUser.setEmail("Test@email.com");
        assertEquals("Test@email.com",TestUser.getEmail());
    }
    @Test
    public void testSetImage(){
        TestUser.setUserimage(TestImage);
        assertEquals(TestImage,TestUser.getImage());
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
