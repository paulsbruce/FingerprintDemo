package io.paulsbruce.dev.fingerprintdemo.appium.webdriver;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;

/**
 * Created by paul on 6/22/17.
 * Implementation for local dev scenarios;
 */

class LocalDevWebDriver extends AndroidDriver {

    private static String getAppiumAddress() {
        String s;
        s = System.getenv("APPIUM_ADDRESS");
        if(s == null)
            s = "paulsbruce-appium.ngrok.io:80";
        return s;
    }

    LocalDevWebDriver() throws Exception {
        super(new URL("http://"+getAppiumAddress()+"/wd/hub"), getCaps()); // for local dev machine, add hosts entry
        this.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    private static DesiredCapabilities getCaps() {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("deviceName", "arbitrary since we don't use UDID"); // still needed to tell appium to not use UDID
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability(CapabilityType.VERSION, "6.0.1");

        // demo hack; need to create a custom gradle copy task instead
        String sFilepath = "app/build/outputs/apk/app-debug.apk";
        if(!(new File(sFilepath)).exists())
            sFilepath = "/var/jenkins_home/workspace/FingerprintDemo-pipeline/app/build/outputs/apk/app-debug.apk";

        String filePath = new File(sFilepath).getAbsolutePath();
        capabilities.setCapability("app", filePath);

        return capabilities;
    }
}
