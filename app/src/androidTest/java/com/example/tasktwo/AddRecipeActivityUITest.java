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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddRecipeActivityUITest {

    @Rule
    public ActivityScenarioRule<AddRecipeActivity> activityRule = new ActivityScenarioRule<>(AddRecipeActivity.class);

    @Before
    public void setUp() {
        Intents.init(); // Initialize intents for checking navigation
    }

    @Test
    public void testSelectImage() {
        onView(withId(R.id.btnSelectImage)).perform(click());

        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_PICK));
        Intents.intended(IntentMatchers.hasType("image/*"));
    }

    @Test
    public void testInputRecipeDetails() {
        onView(withId(R.id.editTitle)).perform(replaceText("Pasta Recipe"));
        onView(withId(R.id.editIngredients)).perform(replaceText("Tomato, Pasta, Cheese"));
        onView(withId(R.id.editInstructions)).perform(replaceText("Boil pasta, add sauce, mix well."));

        onView(withId(R.id.editTitle)).check(matches(withText("Pasta Recipe")));
        onView(withId(R.id.editIngredients)).check(matches(withText("Tomato, Pasta, Cheese")));
        onView(withId(R.id.editInstructions)).check(matches(withText("Boil pasta, add sauce, mix well.")));
    }

    @Test
    public void testSaveRecipeButton() {
        onView(withId(R.id.editTitle)).perform(replaceText("Pizza"));
        onView(withId(R.id.editIngredients)).perform(replaceText("Dough, Tomato, Cheese"));
        onView(withId(R.id.editInstructions)).perform(replaceText("Bake at 180C for 20 mins."));

        onView(withId(R.id.btnSaveRecipe)).perform(click());

        // Check if progress dialog is shown
        onView(withText("Saving Recipe to firebase")).check(matches(isDisplayed()));
    }

    @Test
    public void testExitButtonNavigation() {
        onView(withId(R.id.Exit)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void testRecipeListButtonNavigation() {
        onView(withId(R.id.RecipeList)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(RecipeListActivity.class.getName()));
    }
}



