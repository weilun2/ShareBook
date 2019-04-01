package ca.ualberta.cmput301w19t05.sharebook;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301w19t05.sharebook.activities.BookDetailActivity;
import ca.ualberta.cmput301w19t05.sharebook.activities.MainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class BookDetailTest extends ActivityTestRule<MainActivity> {
    private Solo solo;
    private Activity activity;

    public BookDetailTest() {
        super(MainActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, false, true);

    @Before
    public void setUp() {
        solo = new Solo(getInstrumentation(), rule.getActivity());
        solo.clickOnText("xiao's book2");
    }

    @Test
    public void start() {
        activity = rule.getActivity();
    }

    @Test
    public void changeTile(){
        solo.clickOnView(solo.getView(R.id.title));
        solo.clearEditText(0);
        solo.enterText((EditText) solo.getView(R.id.user_input),"xiao's book2");
        solo.clickOnText("submit");
        solo.assertCurrentActivity("Wrong Activity", BookDetailActivity.class);
    }

    @Test
    public void changeAuthor(){
        solo.clickOnView(solo.getView(R.id.author));
        solo.clearEditText(0);
        solo.enterText((EditText) solo.getView(R.id.user_input),"xiao");
        solo.clickOnText("submit");
        solo.assertCurrentActivity("Wrong Activity", BookDetailActivity.class);
    }

    @Test
    public void changeDescription(){
        solo.clickOnView(solo.getView(R.id.description));
        solo.clearEditText(0);
        solo.enterText((EditText) solo.getView(R.id.user_input),"just the test description");
        solo.clickOnText("submit");
        solo.assertCurrentActivity("Wrong Activity", BookDetailActivity.class);
    }

    @Test
    public void deleteBook(){
        solo.clickOnView(solo.getView(R.id.delete_book));
        solo.clickOnText("Yes");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

}
