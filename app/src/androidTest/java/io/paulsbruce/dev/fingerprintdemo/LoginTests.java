package io.paulsbruce.dev.fingerprintdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.RequiresDevice;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleCallback;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.view.WindowManager;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTests {

    @Rule
    public ActivityTestRule<LoginActivity> testRule = new ActivityTestRule<LoginActivity>(LoginActivity.class) {

        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();

            ActivityLifecycleMonitorRegistry.getInstance().addLifecycleCallback(new ActivityLifecycleCallback() {
                @Override public void onActivityLifecycleChanged(Activity activity, Stage stage) {
                    if (stage == Stage.PRE_ON_CREATE) {
                        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
            });
        }

    };

    /*
    This test suite will be blocked on devices with pin lock (and fingerprint unlock requires this).
    */
    @Test
    public void loginUsingEmail() {

        onView(withId(R.id.email))
                .perform(typeText("foo@example.com"));

        onView(withId(R.id.password))
                .perform(typeText("hello"));

        onView(withId(R.id.email_sign_in_button))
                .perform(click());

        onView(withText(containsString("successfully")))
                .check(matches(isDisplayed()));
    }

    /*
    This test doesn't work because:

    A) Google hsan't provided a way to simulate fingerprint input in Espresso

    B) Architecturally, Espresso isn't suited for hacks like custom ADB commands (tests run on the device, not the workstation)

    ...therefore, for the time being, we have to run with Appium and a custom ADB command to simulate fingerprint input
     */
    //@Test
    @RequiresDevice
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.M)
    public void loginUsingFingerprint() throws IOException {

        // beginning with default activity ('Login')
        onView(withId(R.id.finger_sign_in_button))
                .perform(click());

        // fails because this code is executed on the device, not on the workstation
        Runtime.getRuntime().exec("adb -e emu finger touch 1"); // this doesn't work because tests run on the device

        onView(withText(containsString("successfully")))
                .check(matches(isDisplayed()));
    }
}
