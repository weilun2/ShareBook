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

import ca.ualberta.cmput301w19t05.sharebook.activities.LoginActivity;
import ca.ualberta.cmput301w19t05.sharebook.activities.MainActivity;
import ca.ualberta.cmput301w19t05.sharebook.activities.Register;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

/**
 * Test for login
 */
@RunWith(AndroidJUnit4.class)

public class LoginAndOutTest extends ActivityTestRule<LoginActivity> {
    private Solo solo;

    public LoginAndOutTest() {
        super(LoginActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<LoginActivity> rule
            = new ActivityTestRule<>(LoginActivity.class, false, true);

    @Before
    public void setUp() {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start()  {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testActivity() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }


    @Test
    public void incorrectPasswordCase() {

        solo.enterText((EditText) solo.getView(R.id.email), "liucheng@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "234567");
        solo.clickOnText("Sign in");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    @Test
    public void LoginCorrectCase() {

        solo.enterText((EditText) solo.getView(R.id.email), "liucheng@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "1234567");
        solo.clickOnText("Sign in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

/**
 *  @Test
    public void LogOutCorrectCase() {
        solo.enterText((EditText) solo.getView(R.id.email), "liucheng@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "1234567");
        solo.clickOnText("Sign in");
        assertTrue(solo.waitForText("Attempt to Login ...",1,2000));
        solo.scrollToSide(Solo.RIGHT);
        solo.clickOnView(solo.getView(R.id.log_out));
        solo.clickOnText("yes");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }
*/

    @Test
    public void RegisterCase() {
        solo.clickOnText("Register Now");
        solo.assertCurrentActivity("Wrong Activity", Register.class);
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}

