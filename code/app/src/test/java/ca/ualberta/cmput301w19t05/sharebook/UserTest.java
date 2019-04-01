package ca.ualberta.cmput301w19t05.sharebook;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.User;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private User TestUser1;
    private User TestUser2;

    private Uri TestImage1;
    private Uri TestImage2;
    private Book mBook;
    private List<Long> Rates;

    private User owner;
    private User borrower;


    @Before
    public void setup(){
        TestUser1 = new User("TestID","TestUserName","Test@email.com", TestImage1);
        Rates = new ArrayList<>();
        Rates.add( 1l);
        Rates.add( 2l);
        Rates.add( 3l);
        TestUser2 = new User("TestID2","TestUserName2","Test2@email.com", TestImage2,3,456);
        TestUser2.setRates(Rates);
    }
    @Test
    public void testConstructor(){
      assertEquals("TestID", TestUser1.getUserID());
      assertEquals("TestUserName", TestUser1.getUsername());
      assertEquals("Test@email.com", TestUser1.getEmail());
      assertEquals(TestImage1, TestUser1.getImage());
      assertEquals(3,TestUser2.getRateCount());
      assertEquals(Rates,TestUser2.getRates());
    }
    @Test
    public void testSetUserID(){
        TestUser1.setUserID("TestID");
        assertEquals("TestID", TestUser1.getUserID());
    }
    @Test
    public void testSetUserName(){
        TestUser1.setUsername("TestUserName");
        assertEquals("TestUserName", TestUser1.getUsername());
    }
    @Test
    public void testSetEmail(){
        TestUser1.setEmail("Test@email.com");
        assertEquals("Test@email.com", TestUser1.getEmail());
    }
    @Test
    public void testSetImage(){
        TestUser1.setUserimage(TestImage1);
        assertEquals(TestImage1, TestUser1.getImage());
    }

    @Test
    public void testSetRates(){
        TestUser1.setRates(Rates);
        assertEquals(Rates,TestUser1.getRates());

    }








}
