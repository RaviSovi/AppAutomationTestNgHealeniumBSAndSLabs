package com.project.pageobjects;

import com.project.base.DriverFactory;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {
    protected AppiumDriver driver;

    public BasePage() {
        this.driver = DriverFactory.getDriver();
        PageFactory.initElements(driver, this);
    }
}
