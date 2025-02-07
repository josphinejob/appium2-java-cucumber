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
    @iOSXCUITFindBy(accessibility = "header")
    private WebElement header;

    @AndroidFindBy(id = "android:id/navigationBarBackground")
    @iOSXCUITFindBy(accessibility = "navBar")
    private WebElement navBar;

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
                verifyElementDisplayed(header, elementName);
                break;
            case "navigationbar":
                verifyElementDisplayed(navBar, elementName);
                break;
            default:
                throw new IllegalArgumentException("Unknown element: " + elementName);
        }
    }

    private void verifyElementDisplayed(WebElement element, String elementName) {
        if (element == null) { // Add null check first
            throw new RuntimeException(elementName + " element not found!");
        }
        if (!element.isDisplayed()) {
            throw new RuntimeException(elementName + " element not displayed!");
        }
    }
}