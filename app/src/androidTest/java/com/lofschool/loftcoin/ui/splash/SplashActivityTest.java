package com.lofschool.loftcoin.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.intercepting.SingleActivityFactory;

import com.lofschool.loftcoin.ui.main.MainActivity;
import com.lofschool.loftcoin.ui.welcome.WelcomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {

    @Rule
    public final ActivityTestRule<SplashActivity> rule = new ActivityTestRule<>(
        new SingleActivityFactory<SplashActivity>(SplashActivity.class) {
            @Override
            protected SplashActivity create(Intent intent) {
                final SplashActivity activity = new SplashActivity();
                activity.idling = idling;
                return activity;
            }
        }, false, false);

    private SharedPreferences prefs;

    private TestIdling idling;

    @Before
    public void setUp() throws Exception {
        final Context context = ApplicationProvider.getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        idling = new TestIdling();
        IdlingRegistry.getInstance().register(idling.resource);
    }

    @Test
    public void open_welcome_if_first_run() throws InterruptedException {
        prefs.edit().putBoolean(WelcomeActivity.KEY_SHOW_WELCOME, true).apply();
        rule.launchActivity(new Intent());
        Intents.init();
        Intents.intended(IntentMatchers.hasComponent(WelcomeActivity.class.getName()));
    }

    @Test
    public void open_main_if_start_working_being_clicked() throws InterruptedException {
        prefs.edit().putBoolean(WelcomeActivity.KEY_SHOW_WELCOME, false).apply();
        rule.launchActivity(new Intent());
        Intents.init();
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(idling.resource);
        Intents.release();
    }

    private static class TestIdling implements SplashIdling {

        final CountingIdlingResource resource = new CountingIdlingResource("splash");

        @Override
        public void busy() {
            resource.increment();
        }

        @Override
        public void idle() {
            resource.decrement();
        }

    }

}