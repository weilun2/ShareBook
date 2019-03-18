package ca.ualberta.cmput301w19t05.sharebook;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301w19t05.sharebook.activities.UserProfile;

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

    }
}
