package com.project.base;

import com.project.config.ReadProperties;
import io.appium.java_client.AppiumDriver;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;

import java.util.HashMap;

public class PageBase extends ReadProperties {
    public static String reportFileName;
    public static String Execution = "";

    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    private static final String SAUCELABS_USERNAME = getProperty("slabs.username");
    private static final String SAUCELABS_ACCESS_KEY = getProperty("slabs.accesskey");
    public static final String SAUCELABS_URL = getProperty("slabs.eu-cental.url");

    private static final String BROWSERSTACK_USERNAME = getProperty("bs.username");
    private static final String BROWSERSTACK_ACCESS_KEY = getProperty("bs.accesskey");
    public static final String BROWSERSTACK_URL = getProperty("bs.url");

    private static String getProperty(String key) {
        try {
            return DrivergetProperty(key);
        } catch (Exception e) {
            System.err.println("Failed to load property: " + key);
            return "";
        }
    }

//    public static ChromeOptions getChromeOptions() {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments(
//                "--start-maximized",
//                "--disable-infobars",
//                "--disable-extensions",
//                "--disable-dev-shm-usage",
//                "--no-sandbox",
//                "--remote-allow-origins=*",
//                "--incognito",
//                "--disable-popup-blocking",
//                "--disable-blink-features=AutomationControlled",
//                "--disable-notifications"
//        );
//        return options;
//    }
//
//    public static EdgeOptions getEdgeOptions() {
//        EdgeOptions options = new EdgeOptions();
//        options.addArguments("--inprivate", "--start-maximized");
//        return options;
//    }

    public static DesiredCapabilities getBrowserStackOptionsBasedOnPlatform(String testName, String buildName, String className, String platform) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        HashMap<String, Object> bstackOptions = new HashMap<String, Object>();
        bstackOptions.put("userName", BROWSERSTACK_USERNAME);
        bstackOptions.put("accessKey", BROWSERSTACK_ACCESS_KEY);
        bstackOptions.put("appiumVersion", "1.22.0");
        bstackOptions.put("projectName", "Company Project");
        bstackOptions.put("buildName", buildName);
        bstackOptions.put("sessionName", className + "." + testName);
        bstackOptions.put("selfHeal", "true");
        if(platform.equalsIgnoreCase("android")) {
            capabilities.setCapability("platformName", "android");
            capabilities.setCapability("appium:platformVersion", "12.0");
            capabilities.setCapability("appium:deviceName", "Samsung Galaxy S22 Ultra");
            capabilities.setCapability("appium:app", "bs://03225c6962008560b8977995bea9ad8fdf54f00b");
            capabilities.setCapability("appium:automationName", "UIAutomator2");
        } else if (platform.equalsIgnoreCase("ios")) {
            capabilities.setCapability("platformName", "ios");
            capabilities.setCapability("appium:platformVersion", "16");
            capabilities.setCapability("appium:deviceName", "iPhone 14 Pro Max");
            capabilities.setCapability("appium:automationName", "XCUITest");
            capabilities.setCapability("appium:app", "bs://e9435820325bc6c487f5d67172c98687faa96edb");
        }
        capabilities.setCapability("bstack:options", bstackOptions);

        
        return capabilities;
    }

    public static DesiredCapabilities getSauceLabsOptionsBasedOnPlatform(String testName, String buildName, String className, String platform) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        HashMap<String, Object> options = new HashMap<>();
        options.put("username", SAUCELABS_USERNAME);
        options.put("accessKey", SAUCELABS_ACCESS_KEY);
        options.put("platformName", "Android");  // or "iOS"
        options.put("deviceName", "Android GoogleAPI Emulator");
        options.put("appiumVersion", "1.22.0");
        options.put("platformVersion", "13.0");
        options.put("build", buildName);
        options.put("name", className + "." + testName);
        capabilities.setCapability("sauce:options", options);
        return capabilities;
    }

    public static AppiumDriver getDriver() {
        if (driver.get() == null) throw new IllegalStateException("Appium Driver is not initialized.");
        return driver.get();
    }

    public static void setDriver(AppiumDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static void removeDriver() {
        driver.remove();
    }

    public static void quitDriver() {
        try {
            getDriver().quit();
        } catch (Exception e) {
            System.err.println("Error while quitting driver: " + e.getMessage());
        }
    }

    public static void updateCloudExecutionStatus(AppiumDriver appiumdriver, ITestResult result, String execution) {
        String status;
        String reason = "";

        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> {
                status = "passed";
                reason = "Passed";
            }
            case ITestResult.FAILURE -> {
                status = "failed";
                reason = result.getThrowable() != null ? result.getThrowable().toString() : "Failed";
            }
            default -> {
                status = "skipped";
                reason = result.getThrowable() != null ? result.getThrowable().toString() : "Skipped";
            }
        }

        try {
            if (execution.equalsIgnoreCase("browserstack")) {
                JSONObject executorObject = new JSONObject();
                JSONObject argumentsObject = new JSONObject();
                argumentsObject.put("status", status);
                argumentsObject.put("reason", reason);
                executorObject.put("action", "setSessionStatus");
                executorObject.put("arguments", argumentsObject);
                appiumdriver.executeScript(String.format("browserstack_executor: %s", executorObject));
            } else if (execution.equalsIgnoreCase("saucelabs")) {
                appiumdriver.executeScript("sauce:job-result=" + status);
            }
        } catch (Exception e) {
            System.out.println("Unable to update execution status: " + e.getMessage());
        }
    }

    public static boolean isHealeniumEnabled() throws Exception {
        return Boolean.parseBoolean(DrivergetProperty("use.healenium"));
    }
}