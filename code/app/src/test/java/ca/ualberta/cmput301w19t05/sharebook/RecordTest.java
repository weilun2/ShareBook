package ca.ualberta.cmput301w19t05.sharebook;

import org.junit.Before;
import org.junit.Test;

import java.security.acl.Owner;

import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.Record;
import ca.ualberta.cmput301w19t05.sharebook.models.User;
import static org.junit.Assert.*;

public class RecordTest {
    private Record TestRecord;
    private Book TestBook;
    private User TestOwner;
    private User TestBorrower;
    @Before
    public void setup(){
        TestRecord = new Record(TestBook,TestOwner,TestBorrower);
    }
    @Test
    public void testConstructor(){
        assertEquals(TestBook,TestRecord.getBook());
        assertEquals(TestOwner,TestRecord.getOwner());
        assertEquals(TestBorrower,TestRecord.getBorrower());
    }
    @Test
    public void testSetBook(){
        TestRecord.setBook(TestBook);
        assertEquals(TestBook,TestRecord.getBook());
    }
    @Test
    public void testSetOwner(){
        TestRecord.setOwner(TestOwner);
        assertEquals(TestOwner,TestRecord.getOwner());
    }
    @Test
    public void testSetBorrower(){
        TestRecord.setBorrower(TestBorrower);
        assertEquals(TestBorrower,TestRecord.getBorrower());
    }
}
