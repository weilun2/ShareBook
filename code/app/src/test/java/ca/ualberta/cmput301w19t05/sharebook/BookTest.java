package ca.ualberta.cmput301w19t05.sharebook;


import android.net.Uri;

import org.junit.Before;
import org.junit.Test;

import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.User;

import static org.junit.Assert.assertEquals;
/**
 * Test that setup, set constructor, set title, set author, s, setISBN, set status, set id, and set description of a book
 */

public class BookTest {
    private Book TestBook1;
    private Book TestBook2;
    private Book TestBook3;
    private User TestOwner1;
    private User TestOwner2;
    private User TestOwner3;
    private Uri TestUri;
    @Before
    public void setup(){
        TestBook1 = new Book("Test Title1","Test Author1","1234",TestOwner1);
        TestBook2 = new Book("Test Title2","Test Author2","4567",TestOwner2,"Borrowed","Test Id");

    }
    @Test
    public void testConstructor(){
        assertEquals("Test Title1",TestBook1.getTitle());
        assertEquals("Test Author1",TestBook1.getAuthor());
        assertEquals("1234",TestBook1.getISBN());
        assertEquals(TestOwner1,TestBook1.getOwner());
        assertEquals("Test Title2",TestBook2.getTitle());
        assertEquals("Test Author2",TestBook2.getAuthor());
        assertEquals("4567",TestBook2.getISBN());
        assertEquals(TestOwner2,TestBook2.getOwner());
        assertEquals("Borrowed",TestBook2.getStatus());
        assertEquals("Test Id", TestBook2.getBookId());

    }
    @Test
    public void testSetTitle(){
        TestBook1.setTitle("Test Title1");
        assertEquals("Test Title1",TestBook1.getTitle());
    }
    @Test
    public void testSetAuthor(){
        TestBook1.setAuthor("Test Author1");
        assertEquals("Test Author1",TestBook1.getAuthor());
    }
    @Test
    public void testSetISBN(){
        TestBook1.setISBN("1234");
        assertEquals("1234",TestBook1.getISBN());
    }
    @Test
    public void testSetOwner(){
        TestBook1.setOwner(TestOwner1);
        assertEquals(TestOwner1,TestBook1.getOwner());
    }
    @Test
    public void testSetStatus(){
        TestBook2.setStatus("Borrowed");
        assertEquals("Borrowed",TestBook2.getStatus());
    }
    @Test
    public void testSetId(){
        TestBook2.setBookId("Test Id");
        assertEquals("Test Id",TestBook2.getBookId());
    }
    @Test
    public void testSetDes(){
        TestBook2.setDescription("This is a test");
        assertEquals("This is a test",TestBook2.getDescription());
    }


}