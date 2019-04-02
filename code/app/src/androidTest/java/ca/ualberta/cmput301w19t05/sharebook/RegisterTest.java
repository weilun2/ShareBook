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


import ca.ualberta.cmput301w19t05.sharebook.activities.Register;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

/**
 * Test for register a new account
 */
@RunWith(AndroidJUnit4.class)

public class RegisterTest extends ActivityTestRule<Register> {
    private Solo solo;

    public RegisterTest() {
        super(Register.class, false, true);
    }


    @Rule
    public ActivityTestRule<Register> rule
            = new ActivityTestRule<>(Register.class, false, true);

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
        solo.assertCurrentActivity("Wrong Activity", Register.class);
    }


    @Test
    public void userNameExistCase() {
//
        solo.enterText((EditText) solo.getView(R.id.signup_input_name), "samxiao");
        solo.enterText((EditText) solo.getView(R.id.signup_input_email), "sample@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signup_input_password), "1234567");
        solo.enterText((EditText) solo.getView(R.id.second_input_password), "1234567");
        solo.clickOnText("Register Now");
        assertTrue(solo.waitForText("username exists",1,2000));

    }

    @Test
    public void passwordNotMatchCase() {
//
        solo.enterText((EditText) solo.getView(R.id.signup_input_name), "testName");
        solo.enterText((EditText) solo.getView(R.id.signup_input_email), "sample@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signup_input_password), "1234567");
        solo.enterText((EditText) solo.getView(R.id.second_input_password), "234567");
        solo.clickOnText("Register Now");
        assertTrue(solo.waitForText("two passwords do not match",1,2000));

    }

    @Test
    public void successRegisterCase() {
//
        solo.enterText((EditText) solo.getView(R.id.signup_input_name), "9343");
        solo.enterText((EditText) solo.getView(R.id.signup_input_email), "xiaosam@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signup_input_password), "1234567");
        solo.enterText((EditText) solo.getView(R.id.second_input_password), "1234567");
        solo.clickOnText("Register Now");
        assertTrue(solo.waitForText("Adding you ...",1,2000));
    }


    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}