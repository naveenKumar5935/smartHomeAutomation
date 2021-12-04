package ca.theautomators.it.smarthomeautomation;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ReviewActivityTest {

    @Rule
   public ActivityTestRule<ReviewAcitivity> activityActivityTestRule =  new ActivityTestRule<ReviewAcitivity>(ReviewAcitivity.class);
   // ReviewAcitivity reviewAcitivity;
   ReviewAcitivity activity;
    @Before
    public void setUp() throws Exception {
       // reviewAcitivity = new ReviewAcitivity();
        activity= activityActivityTestRule.getActivity();
        activity.setRatingBarForTest(3);
    }



    @Test
    public void setName(){
        Espresso.onView(withId(R.id.Name)).perform(typeText("naveen"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.btnSubmit)).perform(click());

    }

    @Test
    public void setPhone(){

        Espresso.onView(withId(R.id.Phoneno)).perform(typeText("6478096396"));
        Espresso.onView(withId(R.id.btnSubmit)).perform(click());
    }

    @Test
    public void setEmail(){

        Espresso.onView(withId(R.id.email)).perform(typeText("naveenbti002@gmail.com"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.btnSubmit)).perform(click());
    }

    @Test
    public void setReview(){
        Espresso.onView(withId(R.id.etFeedback)).perform(typeText("my name is naveen"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.btnSubmit)).perform(click());

    }

    @Test
    public void setEverything(){
//        Espresso.onView(withId(R.id.ratingBar)).perform(ReviewAcitivity.settingRatingBarForTesting(3));
        Espresso.onView(withId(R.id.Name)).perform(typeText("naveen"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.Phoneno)).perform(typeText("6478096396"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.email)).perform(typeText("naveenbti002@gmail.com"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.etFeedback)).perform(typeText("my name is naveen"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.btnSubmit)).perform(click());



    }

    @After
    public void tearDown() throws Exception {
    }
}