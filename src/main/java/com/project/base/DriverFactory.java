package com.project.base;

import com.epam.healenium.SelfHealingDriver;
import io.appium.java_client.AppiumDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;

public class DriverFactory extends PageBase {

    @BeforeMethod(alwaysRun = true)
    @Parameters({"execution", "platform"})
    public void setupDriver(String execution, String platform, Method method, ITestContext context) throws Exception {
        Execution = execution;
        String className = context.getAllTestMethods()[0].getRealClass().getSimpleName();
        AppiumDriver mobileDriver = null;
        System.out.println("Test cases are running on: '"+Execution+"' and '"+platform+"' platform.");

        switch (execution.toLowerCase()) {
            case "local" -> {
//                if (browser.equalsIgnoreCase("chrome")) {
//                    WebDriverManager.chromedriver().setup();
//                    webDriver = new org.openqa.selenium.chrome.ChromeDriver(getChromeOptions());
//                } else if (browser.equalsIgnoreCase("edge")) {
//                    webDriver = new org.openqa.selenium.edge.EdgeDriver(getEdgeOptions());
//                }
            }
            case "browserstack" -> {
                mobileDriver = new AppiumDriver(new URL(BROWSERSTACK_URL), getBrowserStackOptionsBasedOnPlatform(method.getName(), context.getSuite().getName(), className, platform));
            }
            case "saucelabs" -> {
                mobileDriver = new AppiumDriver(new URL("http://localhost:8085/wd/hub"), getSauceLabsOptionsBasedOnPlatform(method.getName(), context.getSuite().getName(), className, platform));
            }
        }

        if (isHealeniumEnabled()) {
            System.out.println("Launching with Healenium SelfHealingDriver");
            setDriver((AppiumDriver) SelfHealingDriver.create(mobileDriver));
        } else {
            System.out.println("Launching with regular Appium Driver");
            setDriver(mobileDriver);
        }

       // getDriver().manage().deleteAllCookies();
    }

    @AfterMethod(alwaysRun = true)
    @Parameters({"execution"})
    public void tearDown(String execution, ITestResult result) {
        updateCloudExecutionStatus(getDriver(), result, execution);
        quitDriver();
        removeDriver();
    }

    @BeforeSuite(alwaysRun = true)
    @Parameters({"execution"})
    public void initExtentReportName(String execution, ITestContext context) {
        reportFileName = context.getSuite().getName() + " " + execution + ".html";
    }
}