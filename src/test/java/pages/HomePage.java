package pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator; // Add this import
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.AppiumDriverManager;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.time.Duration;

public class HomePage {

    @AndroidFindBy(id = "android:id/decor_content_parent")
    @iOSXCUITFindBy(accessibility = "Compute Sum")
    private WebElement header;

    @AndroidFindBy(accessibility = "Text")
    @iOSXCUITFindBy(accessibility = "locationStatus")
    private WebElement text;

    public HomePage() {
        RemoteWebDriver driver = AppiumDriverManager.getDriver();

        if (driver == null) {
            throw new IllegalStateException("Driver is not initialized. Ensure AppiumDriverManager.startDriver() is called first.");
        }

        // Initialize PageFactory INSIDE the constructor
        PageFactory.initElements(
                new AppiumFieldDecorator(driver, Duration.ofSeconds(15)),
                this
        );
    }

    public void verifyElementIsDisplayed(String elementName) {
        switch(elementName.toLowerCase()) {
            case "header":
                verifyElementDisplay(header, elementName);
                break;
            case "text":
                verifyElementDisplay(text, elementName);
                break;
            default:
                throw new IllegalArgumentException("Unknown element: " + elementName);
        }
    }

    private void verifyElementDisplay(WebElement element, String elementName) {
        if (element == null) { // Add null check first
            throw new RuntimeException(elementName + " element not found!");
        }
        if (!element.isDisplayed()) {
            throw new RuntimeException(elementName + " element not displayed!");
        }
    }
}