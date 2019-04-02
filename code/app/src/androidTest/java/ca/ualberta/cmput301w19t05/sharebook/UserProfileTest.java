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

import ca.ualberta.cmput301w19t05.sharebook.activities.UserProfile;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)

public class UserProfileTest extends ActivityTestRule<UserProfile>{
    private Solo solo;

    public UserProfileTest(){
        super(UserProfile.class, false,true);
    }


    @Rule
    public ActivityTestRule<UserProfile> rule
            = new ActivityTestRule<>(UserProfile.class, false, true);

    @Before
    public void setUp() {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start(){
        Activity activity = rule.getActivity();
    }

    @Test
    public void successChangeName(){
        solo.clickOnView(solo.getView(R.id.UserName));
        solo.clearEditText(0);
        solo.enterText((EditText) solo.getView(R.id.user_input), "onlyForTest");
        solo.clickOnText("submit");
        assertTrue(solo.waitForText("updating your username...",1,2000));
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
    }

    @Test
    public void failChangeName(){
        solo.clickOnView(solo.getView(R.id.UserName));
        solo.clearEditText(0);
        solo.enterText((EditText) solo.getView(R.id.user_input), "samxiao");
        solo.clickOnText("submit");
        assertTrue(solo.waitForText("username exists",1,2000));
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
    }

    @Test
    public void successChangeEmail(){
        solo.clickOnView(solo.getView(R.id.UserEmail));
        solo.clearEditText(0);
        solo.enterText((EditText) solo.getView(R.id.user_input), "onlyForTest@gmail.com");
        solo.clickOnText("submit");
        assertTrue(solo.waitForText("updating your email...",1,2000));
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
    }

    @Test
    public void failChangeEmail(){
        solo.clickOnView(solo.getView(R.id.UserEmail));
        solo.clearEditText(0);
        solo.enterText((EditText) solo.getView(R.id.user_input), "liucheng@gmail.com");
        solo.clickOnText("submit");
        assertTrue(solo.waitForText("update email failed",1,2000));
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

}
