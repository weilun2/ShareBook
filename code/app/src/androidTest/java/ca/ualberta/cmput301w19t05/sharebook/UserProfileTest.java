package ca.ualberta.cmput301w19t05.sharebook;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301w19t05.sharebook.activities.UserProfile;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)

public class UserProfileTest {
    private String userName;
    private String userEmail;

    @Rule
    public ActivityTestRule<UserProfile> activityRule
            = new ActivityTestRule<> (UserProfile.class);

    @Before
    public void initValidString(){
        userName = "abc";
        userEmail = "abc@adc.com";
    }

    @Test
    public void addText_sameActivity(){
        onView(withId(R.id.userName))
                .perform(typeText(userName));
        onView(withId(R.id.userEmail))
                .perform(typeText(userEmail));

        onView(withText(userName)).check(matches(isDisplayed()));
        onView(withText(userEmail)).check(matches(isDisplayed()));
    }
}
