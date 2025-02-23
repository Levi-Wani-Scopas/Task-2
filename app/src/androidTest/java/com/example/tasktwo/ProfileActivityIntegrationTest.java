package com.example.tasktwo;

import android.content.Intent;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityIntegrationTest {

    @Rule
    public ActivityTestRule<ProfileActivity> activityRule =
            new ActivityTestRule<>(ProfileActivity.class);

    @Test
    public void testUserProfileDataLoads() {
        // Verify that the user's email is displayed
        onView(withId(R.id.userEmail)).check(matches(withText("test@example.com")));
    }

    @Test
    public void testSaveProfileData() {
        // Simulate typing a new name in the EditText
        onView(withId(R.id.editName)).perform(typeText("Jane Doe"));

        // Simulate clicking the "Save" button
        onView(withId(R.id.btnSave)).perform(click());

        // Check if the name is saved and a success Toast message is shown
        onView(withText("Profile updated!")).inRoot(new ToastMatcher()).check(matches(withText("Profile updated!")));
    }

    @Test
    public void testChangeProfilePicture() {
        // Simulate clicking the "Change Picture" button
        onView(withId(R.id.btnChangePic)).perform(click());

        // Verify if the ImageView is displayed
        onView(withId(R.id.profileImage)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testBackButton() {
        // Simulate clicking the "Back" button
        onView(withId(R.id.Back)).perform(click());

        // Verify if the MainActivity is opened
        Intent expectedIntent = new Intent(activityRule.getActivity(), MainActivity.class);
        Intent actualIntent = activityRule.getActivity().getIntent();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }
}
