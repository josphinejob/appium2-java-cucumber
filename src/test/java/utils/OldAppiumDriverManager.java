package utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * AppiumDriverManager is responsible for managing the Appium driver instance.
 * It initializes the driver based on the platform (Android or iOS) with the required capabilities.
 * The driver can be accessed using getDriver() and properly closed using quitDriver().
 */
public class OldAppiumDriverManager {
    private static RemoteWebDriver driver;
    private static final String APPIUM_SERVER = "http://127.0.0.1:4723";

    /**
     * Initializes the Appium driver based on platform-specific capabilities.
     * Reads required properties from ConfigReader (appium.properties file) for non-device specific capabilities.
     * This method runs once before all tests.
     * @throws MalformedURLException if the Appium server URL is incorrect.
     */

    public static void startDriver() throws MalformedURLException {
        if (driver == null) {
            DesiredCapabilities caps = new DesiredCapabilities();

            // Device-specific capabilities are passed via command-line properties
            String platformName = System.getProperty("platformName");
            String platformVersion = System.getProperty("platformVersion");
            String deviceName = System.getProperty("deviceName");
            String appPath = System.getProperty("appPath");

/*// Check for missing capabilities and fall back to defaults from appium.properties if required.
            platformName = platformName.isEmpty() ? ConfigReader.get("platformName") : platformName;
            platformVersion = platformVersion.isEmpty() ? ConfigReader.get("platformVersion") : platformVersion;
            deviceName = deviceName.isEmpty() ? ConfigReader.get("deviceName") : deviceName;
            appPath = appPath.isEmpty() ? ConfigReader.get("appPath") : appPath;
            */

// Throw exception if any required capability is still null or empty after fallback.
            if (platformName == null || platformVersion == null || deviceName == null || appPath == null) {
                throw new IllegalArgumentException("Required command-line properties are missing: platformName, platformVersion, deviceName, udid, appPath");
            }

            // Set device-specific capabilities
            caps.setCapability("platformName", platformName);
            caps.setCapability("platformVersion", platformVersion);
            caps.setCapability("deviceName", deviceName);
            caps.setCapability("app", appPath);
 /*           caps.setCapability("noReset", true);
            caps.setCapability("fullReset", false);*/

            // Set platform-specific capabilities based on device type
            if (platformName.equalsIgnoreCase("Android")) {
                // Android-specific capabilities read from the config file
               /* String automationName = ConfigReader.get("android.automationName");
                if (automationName == null || automationName.isEmpty()) {
                    automationName = "UiAutomator2";  // Default value
                }*/
                caps.setCapability("automationName", "UiAutomator2");
                caps.setCapability("appPackage", ConfigReader.get("android.appPackage"));
                caps.setCapability("appActivity", ConfigReader.get("android.appActivity"));
                /* caps.setCapability("allowTestPackages", true);
               caps.setCapability("enforceAppInstall", true);
                caps.setCapability("adbExecTimeout", 60000);  // Increase timeout*/

                // Initialize Android driver
                driver = new AndroidDriver(new URL(APPIUM_SERVER), caps);
            }
                else if (platformName.equalsIgnoreCase("iOS")) {
                // iOS-specific capabilities read from the config file
              /*  String automationName = ConfigReader.get("ios.automationName");
                if (automationName == null || automationName.isEmpty()) {
                    automationName = "XCUITest";  // Default value
                }*/
                caps.setCapability("automationName", "XCUITest");
                caps.setCapability("bundleId", ConfigReader.get("ios.bundleId"));
                caps.setCapability("useNewWDA", ConfigReader.get("ios.useNewWDA"));
                caps.setCapability("wdaLocalPort", ConfigReader.get("ios.wdaLocalPort"));
                caps.setCapability("autoAcceptAlerts", ConfigReader.get("ios.autoAcceptAlerts"));

                // Initialize iOS driver
                driver = new IOSDriver(new URL(APPIUM_SERVER), caps);
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platformName);
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        }
    }

    /**
     * Returns the initialized Appium driver instance.
     * @return RemoteWebDriver instance.
     */
    public static RemoteWebDriver getDriver() {
        return driver;
    }

    /**
     * Quits the Appium driver if it is initialized.
     * This method runs once after all tests are completed.
     */
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
