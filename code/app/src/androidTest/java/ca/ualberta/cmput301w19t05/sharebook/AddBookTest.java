package ca.ualberta.cmput301w19t05.sharebook;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301w19t05.sharebook.activities.AddBookActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test for adding book
 */
@RunWith(AndroidJUnit4.class)

public class AddBookTest {
    private String title;
    private String author;
    private String description;

    @Rule
    public ActivityTestRule<AddBookActivity> activityRule
            = new ActivityTestRule<>(AddBookActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        title = "this is title";
        author = "this is author";
        description = "this is description";
    }


    @Test
    public void addText_sameActivity(){
        onView(withId(R.id.title))
                .perform(typeText(title));
        onView(withId(R.id.author))
                .perform(typeText(author));
        onView(withId((R.id.description)))
                .perform(typeText(description));

        onView(withId(R.id.submit)).perform(click());

        onView(withText(title)).check(matches(isDisplayed()));
        onView(withText(author)).check(matches(isDisplayed()));
        onView(withText(description)).check(matches(isDisplayed()));


    }

}
