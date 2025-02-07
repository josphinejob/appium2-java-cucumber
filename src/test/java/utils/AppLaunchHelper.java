package utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class AppLaunchHelper {

    private static final List<String> ANDROID_ALERT_XPATHS = Arrays.asList(
            "//*[contains(@text, 'Allow while using the app')]",  // Location permission
            "//*[contains(@text, 'Are you over 18')]",            // Age verification
            "//*[contains(@text, 'What's New')]",                 // Bottom sheet title
            "//*[contains(@text, 'OK')]"                          // Generic alert dismiss
    );

    private static final List<String> IOS_ALERT_XPATHS = Arrays.asList(
            "//*[contains(@name, 'Allow While Using App')]",  // Location permission
            "//*[contains(@name, 'Are you over 18')]",        // Age verification
            "//*[contains(@name, 'What's New')]",             // Bottom sheet title
            "//*[contains(@name, 'OK')]"                      // Generic alert dismiss
    );

    private static final List<String> ANDROID_DISMISS_BUTTON_XPATHS = Arrays.asList(
            "//*[contains(@text, 'Allow while using the app')]",  // Matches location permission
            "//*[contains(@text, 'Proceed')]",                   // Matches age verification
            "//*[contains(@text, 'Close')]",                     // Matches bottom sheet
            "//*[contains(@text, 'OK')]"                         // Matches generic alerts
    );

    private static final List<String> IOS_DISMISS_BUTTON_XPATHS = Arrays.asList(
            "//*[contains(@name, 'Allow While Using App')]",  // Matches location permission
            "//*[contains(@name, 'Proceed')]",               // Matches age verification
            "//*[contains(@name, 'Close')]",                 // Matches bottom sheet
            "//*[contains(@name, 'OK')]"                     // Matches generic alerts
    );

    private static final RemoteWebDriver driver = AppiumDriverManager.getDriver();
    private static final int RETRY_ATTEMPTS = 3;
    private static final Duration LONG_RETRY_INTERVALS = Duration.ofSeconds(2);

    public AppLaunchHelper() {
    }

    /**
     * Handles all alerts and permissions during app launch.
     */
    public static void handleAppAlertsAndPermissions() {
        if (driver instanceof AndroidDriver) {
            clickIfExists(By.xpath("//*[contains(@text, 'Share My Location')]"));
        } else if (driver instanceof IOSDriver) {
            clickIfExists(By.xpath("//*[contains(@name, 'Share My Location')]"));
        }

        List<String> alertXpaths = driver instanceof AndroidDriver ? ANDROID_ALERT_XPATHS : IOS_ALERT_XPATHS;
        List<String> dismissButtonXpaths = driver instanceof AndroidDriver ? ANDROID_DISMISS_BUTTON_XPATHS : IOS_DISMISS_BUTTON_XPATHS;

        for (int attempt = 0; attempt < RETRY_ATTEMPTS; attempt++) {
            if (checkForHomePage()) break;

            for (int i = 0; i < alertXpaths.size(); i++) {
                By alertLocator = By.xpath(alertXpaths.get(i));
                By dismissButtonLocator = By.xpath(dismissButtonXpaths.get(i));

                if (isElementPresent(alertLocator)) {
                    clickIfExists(dismissButtonLocator);
                }
            }

            if (checkForHomePage()) break;
        }

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                        driver instanceof AndroidDriver ?
                                By.xpath("//*[contains(@text, 'Log In')]") :
                                By.xpath("//*[contains(@name, 'Log In')]")
                ));
    }

    /**
     * Checks if the home page is displayed by looking for the "Log In" button.
     */
    private static boolean checkForHomePage() {
        By homePageLocator = driver instanceof AndroidDriver ?
                By.xpath("//*[contains(@text, 'Log In')]") :
                By.xpath("//*[contains(@name, 'Log In')]");
        return isElementPresent(homePageLocator);
    }

    /**
     * Checks if an element is present on the screen.
     */
    private static boolean isElementPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /**
     * Clicks an element if it exists.
     */
    private static void clickIfExists(By locator) {
        try {
            if (isElementPresent(locator)) {
                driver.findElement(locator).click();
            }
        } catch (Exception e) {
            System.out.println("Error clicking element: " + e.getMessage());
        }
    }
}
