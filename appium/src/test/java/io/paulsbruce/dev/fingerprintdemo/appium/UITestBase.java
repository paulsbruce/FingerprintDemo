package io.paulsbruce.dev.fingerprintdemo.appium;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;

import io.paulsbruce.dev.fingerprintdemo.appium.webdriver.WebDriverFactory;

/**
 * Created by paul on 6/22/17.
 */

@Category({UITest.class})
public class UITestBase {

    protected WebDriver driver;

    @Before
    public void setUp() {
        driver = WebDriverFactory.getDriver();

        //Context appContext = InstrumentationRegistry.getTargetContext();

        //assertEquals("io.paulsbruce.dev.fingerprintdemo", appContext.getPackageName());

        // *** stretch goal *** we should also attach debugger to app code running on device

    }

    @After
    public void End() {
        driver.quit();
    }
}
