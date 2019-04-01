package ca.ualberta.cmput301w19t05.sharebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Test;

import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.User;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;
import static org.junit.Assert.assertEquals;

public class FirebaseHandlerTest {
    private FirebaseHandler testHandler;
    private Context testConstent;
    private FirebaseDatabase testDatabase;
    private DatabaseReference testRef;
    private FirebaseUser testuser;
    private FirebaseAuth testAuth;
    private StorageReference teststorageRef;
    private Uri TestImage1;
    private User testUser;
    private Bitmap testbitmap;
    private Book TestBook;
    private LatLng TestLocation;
    private String testid;
   @ Before
    public void setup(){
       testHandler = new FirebaseHandler(testConstent);
       this.testDatabase = FirebaseDatabase.getInstance();
       this.testRef = testDatabase.getReference();
       testUser = new User("TestID","TestUserName","Test@email.com", TestImage1);
       TestBook = new Book("Test Title2","Test Author2","4567",testUser,"Borrowed","Test Id");
       TestLocation =  new LatLng(51.6416,-113.25);
   }
   @ Test
    public void testAddUsernameEmailTuple(){
       testHandler.addUsernameEmailTuple(testUser);

   }
   @ Test
    public void testUploadImage(){
       testHandler.uploadImage("TestImage",testbitmap);
   }

   @ Test
    public void testAddBook(){

       testHandler.addBook(TestBook);

       assertEquals("Test Id",testid);
   }
   @ Test
    public void testAddLocation(){
       testHandler.addLocation(TestBook,TestLocation);

   }

   @ Test
    public void testChangeBookStatus(){
       testHandler.changeBookStatus(TestBook,"Avaiable");
   }


}

