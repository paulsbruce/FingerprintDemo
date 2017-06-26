package io.paulsbruce.dev.fingerprintdemo.appium.scenarios;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import java.io.IOException;

import io.paulsbruce.dev.fingerprintdemo.appium.UITestBase;

public class LoginTests extends UITestBase {

    @Test
    public void loginUsingEmail()
    {
        // beginning with default activity ('Login')
        driver.findElement(By.id("email")).sendKeys("foo@example.com");
        driver.findElement(By.id("password")).sendKeys("hello");
        driver.findElement(By.id("email_sign_in_button")).click();

        Assert.assertTrue(driver.findElement(By.xpath("//*[contains(@text,'successfully')]")).isDisplayed());
    }

    @Test
    public void loginUsingFingerprint() {

        // beginning with default activity ('Login')

        driver.findElement(By.id("finger_sign_in_button")).click();

        try {
            Runtime.getRuntime().exec("adb -e emu finger touch 1");
        } catch(IOException e) {
        }

        Assert.assertTrue(driver.findElement(By.xpath("//*[contains(@text,'successfully')]")).isDisplayed());
    }
}