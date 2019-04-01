package ca.ualberta.cmput301w19t05.sharebook;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301w19t05.sharebook.activities.MainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class BookDetailBySearchTest extends ActivityTestRule<MainActivity> {
    private Solo solo;
    private Activity activity;

    public BookDetailBySearchTest(){
        super(MainActivity.class,false,true);
    }

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,false,true);

    @Before
    public void setUp(){
        solo = new Solo(getInstrumentation(), rule.getActivity());
        solo.clickOnText("Borrowing");
        solo.clickOnView(solo.getView(R.id.SearchBar));
        solo.enterText((EditText) solo.getView(R.id.SearchBar),"xiao's book");
        solo.clickOnText("xiap's book");
    }

    @Test
    public void start(){
        activity = rule.getActivity();
    }
}
