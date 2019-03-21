package ca.ualberta.cmput301w19t05.sharebook;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301w19t05.sharebook.activities.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)

public class LoginTest {
    private String loginEmail;
    private String password;

    @Rule
    public ActivityTestRule<LoginActivity> activityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initValidString(){
        loginEmail = "abc@abc.com";
        password = "1234567";
    }

    @Test
    public void addText_sameActivity(){
        onView(withId(R.id.email)).perform(typeText(loginEmail));
        onView(withId(R.id.password)).perform(typeText(password));

        //onView(withId(R.id.email_sign_in_button)).perform(click());

        onView(withText(loginEmail)).check(matches(isDisplayed()));
        //onView(withText(password)).check((matches(isDisplayed())));


    }
}
