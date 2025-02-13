package utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class AppLaunchHelper {

    private static final List<String> ALERT_XPATHS = Arrays.asList(
            "//*[contains(@text, 'Allow while using the app')]",  // Android Location permission
            "//*[contains(@name, 'Allow While Using App')]",       // iOS Location permission
            "//*[contains(@text, 'Are you over 18')]",             // Android Age verification
            "//*[contains(@name, 'Are you over 18')]",             // iOS Age verification
            "//*[contains(@text, 'OK')]",                          // Generic alert dismiss (Android)
            "//*[contains(@name, 'OK')]"                           // Generic alert dismiss (iOS)
    );

    private static final RemoteWebDriver driver = AppiumDriverManager.getDriver();
    private static final int RETRY_ATTEMPTS = 1; // Failsafe Mechanism: Retries before proceeding if number > 1
    private static final Duration WAIT_TIME = Duration.ofSeconds(5);

    public AppLaunchHelper() {
    }

    /**
     * Handles alerts and permissions dynamically.
     */
    public static void handleAppAlertsAndPermissions() {
        if (driver instanceof IOSDriver) {
            System.out.println("iOS detected, relying on autoAcceptAlerts if enabled.");
            return; // No need to handle manually for iOS if autoAcceptAlerts is set
        }

        for (int attempt = 0; attempt < RETRY_ATTEMPTS; attempt++) {
            if (checkForHomePage()) break;

            for (String xpath : ALERT_XPATHS) {
                clickIfExists(By.xpath(xpath));
            }

            if (checkForHomePage()) break;
        }

        // Wait for home screen
        new WebDriverWait(driver, WAIT_TIME)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text, 'Log In')]")));
    }

    /**
     * Checks if the home page is displayed by looking for the "Log In" button.
     */
    private static boolean checkForHomePage() {
        By homePageLocator = By.xpath("//*[contains(@text, 'Log In')]");
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
                WebElement element = driver.findElement(locator);
                element.click();
                System.out.println("Clicked on: " + locator.toString());
            }
        } catch (Exception e) {
            System.out.println("Error clicking element: " + e.getMessage());
        }
    }
}
