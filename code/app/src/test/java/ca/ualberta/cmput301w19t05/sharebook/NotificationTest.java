package ca.ualberta.cmput301w19t05.sharebook;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import ca.ualberta.cmput301w19t05.sharebook.models.Notification;
import ca.ualberta.cmput301w19t05.sharebook.models.User;

public class NotificationTest {
    private Notification noti1;
    private User sender;
    private User receiver;
    @Before
    public void setup(){
        noti1 = new Notification("Test message", sender,receiver);
    }
    @Test
    public void testConstructor(){
        assertEquals("Test message", noti1.getMessage() );
        assertEquals( sender,noti1.getSender());
        assertEquals(receiver,noti1.getReceiver());
    }
    @Test
    public void testSetSender(){
        noti1.setSender(sender);
        assertEquals(sender,noti1.getSender());
    }
    @Test
    public void testSetReceiver(){
        noti1.setReceiver(receiver);
        assertEquals(receiver,noti1.getReceiver());
    }
    @Test
    public void testSetMessage(){
        noti1.setMessage("Test message set");
        assertEquals("Test message set",noti1.getMessage());
    }
    @Test
    public void testSetSeen(){
        noti1.setSeen(Boolean.TRUE);
        assertEquals(Boolean.TRUE,noti1.getSeen());
    }
}
