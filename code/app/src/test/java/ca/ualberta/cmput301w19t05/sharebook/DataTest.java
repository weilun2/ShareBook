package ca.ualberta.cmput301w19t05.sharebook;

import org.junit.Before;
import org.junit.Test;

import ca.ualberta.cmput301w19t05.sharebook.models.Data;
import static org.junit.Assert.assertEquals;

public class DataTest {
    private Data TestData1;
    private Data TestData2;
    @Before
    public void setup(){
        TestData1 = new Data("Test Id 1","Test Request Type 1","Test Sender 1","Test Receiver 1");
        TestData2 = new Data("Test Id 2","Test Request Type 2","Test Sender 2","Test Receiver 2","Test Book Name 2","Test Sender Name 2");

    }
    @Test
    public void testConstructor(){
        assertEquals("Test Id 1",TestData1.getBookId());
        assertEquals("Test Request Type 1",TestData1.getRequestType());
        assertEquals("Test Sender 1",TestData1.getSender());
        assertEquals("Test Receiver 1",TestData1.getReceiver());
        assertEquals("Test Id 2",TestData2.getBookId());
        assertEquals("Test Request Type 2",TestData2.getRequestType());
        assertEquals("Test Sender 2",TestData2.getSender());
        assertEquals("Test Receiver 2",TestData2.getReceiver());
        assertEquals("Test Book Name 2",TestData2.getBookName());
        assertEquals("Test Sender Name 2",TestData2.getSenderName());
    }
    @Test
    public void testSetBookId(){
        TestData1.setBookId("Test Id 3");
        assertEquals("Test Id 3",TestData1.getBookId());
    }
    @Test
    public void testSetRequestType(){
        TestData1.setRequestType("Test Request Type 3");
        assertEquals("Test Request Type 3",TestData1.getRequestType());
    }
    @Test
    public void testSetSender(){
        TestData1.setSender("Test Sender 3");
        assertEquals("Test Sender 3",TestData1.getSender());
    }
    @Test
    public void testSetReceiver(){
        TestData1.setReceiver("Test Receiver 3");
        assertEquals("Test Receiver 3",TestData1.getReceiver());
    }
    @Test
    public void testSetBookName(){
        TestData2.setBookName("Test Book Name 3");
        assertEquals("Test Book Name 3",TestData2.getBookName());
    }
    @Test
    public void testSetSenderName(){
        TestData2.setSenderName("Test Sender Name 3");
        assertEquals("Test Sender Name 3",TestData2.getSenderName());
    }
}
