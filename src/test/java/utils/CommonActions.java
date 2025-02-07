package utils;  // You can also put this in the utils package if you prefer

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class CommonActions {

    private final RemoteWebDriver driver;

    public CommonActions() {
        this.driver = AppiumDriverManager.getDriver();
    }

    public void acceptAlert() {
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    public void dismissAlert() {
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }


    // Close the keyboard (common for mobile tests)
    public void closeKeyboard() {
        // logic to close the keyboard, for instance:
        // driver.hideKeyboard();
        System.out.println("Closing the keyboard...");
    }

    // Click the back button (common for both Android and iOS)
    public void clickBackButton() {
        // logic to go back (might use driver.navigate().back() or other methods based on platform)
        System.out.println("Clicking the back button...");
    }

    // Any other common actions like reset, logout, etc.
    public void resetApp() {
        // Reset the app if required for the tests
        System.out.println("Resetting the app...");
    }

    // Switch to WebView context
    public void switchToWebView() {
        // Cast to AndroidDriver or IOSDriver depending on your app's platform
        if (driver instanceof AndroidDriver) {
            AndroidDriver androidDriver = (AndroidDriver) driver;
            Set<String> contexts = androidDriver.getContextHandles();
            for (String context : contexts) {
                if (context.contains("WEBVIEW")) {
                    androidDriver.context(context); // Switch to WebView context
                    break;
                }
            }
        } else if (driver instanceof IOSDriver) {
            IOSDriver iosDriver = (IOSDriver) driver;
            Set<String> contexts = iosDriver.getContextHandles();
            for (String context : contexts) {
                if (context.contains("WEBVIEW")) {
                    iosDriver.context(context); // Switch to WebView context
                    break;
                }
            }
        }
    }

    // Switch back to Native App context
    public void switchToNativeContext() {
        if (driver instanceof AndroidDriver) {
            AndroidDriver androidDriver = (AndroidDriver) driver;
            androidDriver.context("NATIVE_APP"); // Switch to Native App context
        } else if (driver instanceof IOSDriver) {
            IOSDriver iosDriver = (IOSDriver) driver;
            iosDriver.context("NATIVE_APP"); // Switch to Native App context
        }
    }

    public static String takeScreenshot(WebDriver driver, String platformName) throws IOException {
        // 1. Create screenshots directory if it doesn't exist
        File directory = new File("screenshots");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 2. Generate timestamp for unique filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());

        // 3. Create filename with platform and timestamp
        String fileName = String.format("%s_%s.png", platformName.toLowerCase(), timeStamp);
        File destination = new File(directory, fileName);

        try {
            // 4. Take screenshot and save to file
            if (driver instanceof TakesScreenshot) {
                TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
                File screenshotFile = screenshotDriver.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshotFile, destination);

                // 5. Return absolute path for reporting
                return destination.getAbsolutePath();
            } else {
                throw new UnsupportedOperationException("Driver does not support screenshot capture.");
            }
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
            throw e; // Re-throw to handle in test execution
        }
    }

}
