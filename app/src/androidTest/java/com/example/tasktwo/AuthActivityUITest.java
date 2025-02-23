package com.example.tasktwo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AuthActivityUITest {

    @Rule
    public ActivityScenarioRule<AuthActivity> activityRule = new ActivityScenarioRule<>(AuthActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void testLoginButtonDisplayed() {
        onView(withId(R.id.login)).check(matches(isDisplayed()));
    }

    @Test
    public void testSuccessfulLogin() {
        // Enter valid email and password
        onView(withId(R.id.email)).perform(replaceText("test@example.com"));
        onView(withId(R.id.password)).perform(replaceText("password123"));

        // Click login button
        onView(withId(R.id.login)).perform(click());

        // Verify that MainActivity is launched
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void testUnsuccessfulLogin() {
        // Enter invalid credentials
        onView(withId(R.id.email)).perform(replaceText("invalid@example.com"));
        onView(withId(R.id.password)).perform(replaceText("wrongpassword"));

        // Click login button
        onView(withId(R.id.login)).perform(click());

        // Check if error message appears
        onView(withText("Login failed")).check(matches(isDisplayed()));
    }
}

