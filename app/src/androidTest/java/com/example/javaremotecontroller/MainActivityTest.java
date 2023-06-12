package com.example.javaremotecontroller;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

public class MainActivityTest {
    Context ctx;
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void init() {
        ActivityScenario<MainActivity> scenario = mActivityRule.getScenario();
        System.out.println("MainActivity 测试");
    }

    @Test
    public void testContext() {
        // Context of the app under test.
        ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.javaremotecontroller", ctx.getPackageName());
    }

    @Test
    public void testMenuClick() {
        onView(withId(R.id.bottom_nav_dashboard_wrapper)).perform(click());
    }
}
