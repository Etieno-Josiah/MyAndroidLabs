package com.example.etienosandroidlabs;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**This is a test to check if password has a letter in it
     *
     */
    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatEditText = onView( withId(R.id.editPassword) );
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView( withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("You shall not pass!")));
    }

    /**This is a test to check if password has an uppercase letter in it.
     *
     */
    @Test
    public void testFindMissingUpperCase(){
        ViewInteraction appCompactEdit = onView( withId(R.id.editPassword));
        appCompactEdit.perform(replaceText("pasword123#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView( withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**This is a test to check if password has an lowercase letter in it.
     *
     */
    @Test
    public void testFindMissingLowerCase(){
        ViewInteraction appCompactEdit = onView( withId(R.id.editPassword));
        appCompactEdit.perform(replaceText("PASSWORD123#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView( withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**This is a test to check if password has a digit in it.
     *
     */
    @Test
    public void testFindMissingDigit(){
        ViewInteraction appCompactEdit = onView( withId(R.id.editPassword));
        appCompactEdit.perform(replaceText("PASSword#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView( withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**This is a test to check if password has a special character in it.
     *
     */
    @Test
    public void testFindMissingSpecialCharacter(){
        ViewInteraction appCompactEdit = onView( withId(R.id.editPassword));
        appCompactEdit.perform(replaceText("PASSword123"), closeSoftKeyboard());

        ViewInteraction materialButton = onView( withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    @Test
    public void testCorrectPassword(){
        ViewInteraction appCompactEdit = onView( withId(R.id.editPassword));
        appCompactEdit.perform(replaceText("PASSword123@#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView( withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView));
        textView.check(matches(withText("Your password is complex enough")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
