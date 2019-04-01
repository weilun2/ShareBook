package ca.ualberta.cmput301w19t05.sharebook;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301w19t05.sharebook.activities.Register;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)

public class RegisterTest {
    private String sign_up_name;
    private String sign_up_email;
    private String sign_up_password;
    private String second_password;

    @Rule
    public ActivityTestRule<Register> activityTestRule
            = new ActivityTestRule<>(Register.class);

    @Before
    public void initVaildString(){
        sign_up_name = "abc";
        sign_up_email = "abc@abc.com";
        sign_up_password = "1234567";
        second_password = "1234567";
    }

    @Test
    public void addText_sameAcitivity(){
        onView(withId(R.id.signup_input_name)).perform(typeText(sign_up_name));
        onView(withId(R.id.signup_input_email)).perform(typeText(sign_up_email));
        onView(withId(R.id.signup_input_password)).perform(typeText(sign_up_password));
        onView(withId(R.id.second_input_password)).perform(typeText(second_password));

        onView(withId(R.id.submut_register)).perform(click());

        onView(withText(sign_up_name)).check(matches(isDisplayed()));
        onView(withText(sign_up_email)).check(matches(isDisplayed()));
        onView(withText(sign_up_password)).check(matches(isDisplayed()));
        onView(withText(second_password)).check(matches(isDisplayed()));
    }
}
