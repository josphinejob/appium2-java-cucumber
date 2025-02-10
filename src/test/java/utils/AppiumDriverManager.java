package utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AppiumDriverManager {
    private static RemoteWebDriver driver;

    /*Indicates whether the Appium driver has already been initialized, to control fullReset and noReset capabilities.
    fullReset to reset the app state is required only for the first installation while creating the driver.*/
    private static boolean isDriverInitialized = false;

    public static void startDriver() throws MalformedURLException {
        quitDriver();
        if (driver == null) {
            DesiredCapabilities caps = new DesiredCapabilities();
            // Device-specific capabilities are passed via command-line properties
            String platformName = System.getProperty("platformName");
            String platformVersion = System.getProperty("platformVersion");
            String deviceName = System.getProperty("deviceName");
            String udId = System.getProperty("udid");
            String appPath = System.getProperty("appPath");

            // Fallback to config
            platformName = (platformName == null || platformName.isEmpty())
                    ? ConfigReader.get("platformName")
                    : platformName;
            platformVersion = (platformVersion == null || platformVersion.isEmpty())
                    ? ConfigReader.get("platformVersion")
                    : platformVersion;
            deviceName = (deviceName == null || deviceName.isEmpty())
                    ? ConfigReader.get("deviceName")
                    : deviceName;
            udId = (udId == null || udId.isEmpty())
                    ? ConfigReader.get("udId")
                    : udId;
            appPath = (appPath == null || appPath.isEmpty())
                    ? ConfigReader.get("appPath")
                    : appPath;

            // Throw exception if any required capability is still null or empty after fallback.
            if (platformName == null || platformVersion == null || deviceName == null || appPath == null) {
                throw new IllegalArgumentException("Required command-line properties are missing: platformName, platformVersion, deviceName, appPath");
            }

            caps.setCapability("appium:app", appPath);
            caps.setCapability("appium:autoGrantPermissions", "true");
            caps.setCapability("platformName", platformName);
            caps.setCapability("appium:platformVersion", platformVersion);


            // Set platform-specific capabilities based on device type
            if (platformName.equalsIgnoreCase("Android")) {
                caps.setCapability("appium:deviceName", deviceName);
                caps.setCapability("appium:automationName", "UiAutomator2");
                caps.setCapability("appium:appPackage", ConfigReader.get("android.appPackage"));
                caps.setCapability("appium:appActivity", ConfigReader.get("android.appActivity"));
                if (isDriverInitialized){
                    caps.setCapability("appium:fullReset", "false");
                    caps.setCapability("appium:noReset", "true");
                } else {
                    caps.setCapability("appium:fullReset", "true");
                }
                driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);
                isDriverInitialized = true;
            } else if (platformName.equalsIgnoreCase("iOS")) {
                caps.setCapability("appium:udid", udId);
                caps.setCapability("automationName", "XCUITest");
                caps.setCapability("bundleId", ConfigReader.get("ios.bundleId"));
                caps.setCapability("useNewWDA", ConfigReader.get("ios.useNewWDA"));
                caps.setCapability("wdaLocalPort", ConfigReader.get("ios.wdaLocalPort"));
                caps.setCapability("autoAcceptAlerts", ConfigReader.get("ios.autoAcceptAlerts"));
                if (isDriverInitialized){
                    caps.setCapability("appium:fullReset", "false");
                    caps.setCapability("appium:noReset", "true");
                } else {
                    caps.setCapability("appium:fullReset", "true");
                }
                driver = new IOSDriver(new URL("http://127.0.0.1:4723"), caps);
                isDriverInitialized = true;
            }
        }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }

    public static RemoteWebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
