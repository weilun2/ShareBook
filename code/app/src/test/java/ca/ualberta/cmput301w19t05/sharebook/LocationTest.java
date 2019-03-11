package ca.ualberta.cmput301w19t05.sharebook;

import ca.ualberta.cmput301w19t05.sharebook.models.Location;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class LocationTest {
    private Location TestLocation;
    @Before
    public void setup(){
        TestLocation = new Location(69.34689,133.79863,"Edmonton","T6G2H6","Test Place");
    }
    @Test
    public void testConstructor(){
        assertEquals(69.34689,TestLocation.getLet(),0);
        assertEquals(133.79863,TestLocation.getLon(),0);
        assertEquals("Edmonton",TestLocation.getCity());
        assertEquals("T6G2H6",TestLocation.getPostalCode());
        assertEquals("Test Place",TestLocation.getDescription());
    }
    @Test
    public void testSetLet(){
        TestLocation.setLet(69.34689);
        assertEquals(69.34689,TestLocation.getLet(),0);
    }
    @Test
    public void testSetLon(){
        TestLocation.setLon(133.79863);
        assertEquals(133.79863,TestLocation.getLon(),0);
    }
    @Test
    public void testSetCity(){
        TestLocation.setCity("Edmonton");
        assertEquals("Edmonton",TestLocation.getCity());
    }
    @Test
    public void testSetPostCode(){
        TestLocation.setPostalCode("T6G2H6");
        assertEquals("T6G2H6",TestLocation.getPostalCode());
    }
    @Test
    public void testSetDes(){
        TestLocation.setDescription("Test Place");
        assertEquals("Test Place",TestLocation.getDescription());
    }
}
