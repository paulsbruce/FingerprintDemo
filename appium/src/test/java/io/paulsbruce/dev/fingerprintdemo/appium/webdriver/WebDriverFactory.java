package io.paulsbruce.dev.fingerprintdemo.appium.webdriver;

import org.openqa.selenium.WebDriver;

/**
 * Created by paul on 6/22/17.
 */

public class WebDriverFactory {

    public static WebDriver getDriver() throws Exception {
        WebDriver driver = null;
        try {
            driver = new LocalDevWebDriver();
        } catch(Exception ex) {
            throw new Exception("APPIUM_ADDRESS: " + System.getenv("APPIUM_ADDRESS"), ex);
        }
        assert driver != null;
        return driver;
    }



}
