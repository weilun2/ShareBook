package ca.ualberta.cmput301w19t05.sharebook;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301w19t05.sharebook.activities.AddBookActivity;
import ca.ualberta.cmput301w19t05.sharebook.activities.ScanActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AddBookTest extends ActivityTestRule<AddBookActivity>{
    private Solo solo;

    public AddBookTest(){
        super(AddBookActivity.class, false,true);
    }

    @Rule
    public ActivityTestRule<AddBookActivity> rule
            = new ActivityTestRule<>(AddBookActivity.class, false, true);

    @Before
    public void setUp() {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testActivity() {
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
    }

    @Test
    public void EmptyTitleCase(){
        solo.enterText((EditText) solo.getView(R.id.title), "");
        solo.enterText((EditText) solo.getView(R.id.author), "sam");
        solo.enterText((EditText) solo.getView(R.id.ISBN), "1234567");
        solo.enterText((EditText) solo.getView(R.id.description), "this is the sample description");
        solo.clickOnText("Submit");
        assertTrue(solo.waitForText("Please enter the Title",1,2000));
    }

    @Test
    public void EmptyAuthorCase(){
        solo.enterText((EditText) solo.getView(R.id.title), "testTile");
        solo.enterText((EditText) solo.getView(R.id.author), "");
        solo.enterText((EditText) solo.getView(R.id.ISBN), "1234567");
        solo.enterText((EditText) solo.getView(R.id.description), "this is the sample description");
        solo.clickOnText("Submit");
        assertTrue(solo.waitForText("Please enter the Author",1,2000));
    }

    @Test
    public void EmptyISBNCase(){
        solo.enterText((EditText) solo.getView(R.id.title), "testTile");
        solo.enterText((EditText) solo.getView(R.id.author), "sam");
        solo.enterText((EditText) solo.getView(R.id.ISBN), "");
        solo.enterText((EditText) solo.getView(R.id.description), "this is the sample description");
        solo.clickOnText("Submit");
        assertTrue(solo.waitForText("Please enter the ISBN",1,2000));
    }

    @Test
    public void uploadPhotoCase(){
        solo.clickOnText("Upload Photograph");
    }

    @Test
    public void successAddCase() {
//
        solo.enterText((EditText) solo.getView(R.id.title), "testTile");
        solo.enterText((EditText) solo.getView(R.id.author), "sam");
        solo.enterText((EditText) solo.getView(R.id.ISBN), "1234567");
        solo.enterText((EditText) solo.getView(R.id.description), "this is the sample description");
        solo.clickOnText("Submit");
    }


    @Test
    public void scanCase(){
        solo.clickOnText("Scan");
        solo.assertCurrentActivity("Wrong Activity", ScanActivity.class);
    }


    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
