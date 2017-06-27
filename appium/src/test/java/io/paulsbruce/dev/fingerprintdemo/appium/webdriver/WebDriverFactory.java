package io.paulsbruce.dev.fingerprintdemo.appium.webdriver;

import org.openqa.selenium.WebDriver;

import java.util.logging.Logger;

/**
 * Created by paul on 6/22/17.
 * A factory to provide various web drivers depending on dev/ci/cloud environments; so far, only dev
 */

public class WebDriverFactory {

    public static WebDriver getDriver() {
        WebDriver driver = null;
        try {
            driver = new LocalDevWebDriver();
        } catch(Exception ex) {
            Logger.getAnonymousLogger().info("APPIUM_ADDRESS: " + System.getenv("APPIUM_ADDRESS"));
            System.err.println("APPIUM_ADDRESS: " + System.getenv("APPIUM_ADDRESS"));
            System.err.println(ex.toString());
        }
        assert driver != null;
        return driver;
    }



}
