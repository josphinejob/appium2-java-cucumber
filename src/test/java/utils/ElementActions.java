package utils;

import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.Collections;
import org.apache.commons.io.FileUtils;
import java.util.Set;

public class ElementActions {
    private final RemoteWebDriver driver;

    public ElementActions() {
        this.driver = AppiumDriverManager.getDriver();
    }

    public void tapElement(By locator) {
        WebElement element = driver.findElement(locator);
        element.click();
    }

    public void longPressElement(By locator) {
        WebElement element = driver.findElement(locator);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        // Create a sequence of actions to perform the long press
        Sequence longPressSequence = new Sequence(finger, 0);

        // Move the finger to the element location
        longPressSequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), element.getLocation().getX(), element.getLocation().getY()));
        longPressSequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // Hold the press for 2 seconds (you can change this duration as needed)
        longPressSequence.addAction(finger.createPointerMove(Duration.ofMillis(2000), PointerInput.Origin.viewport(), 0, 0));

        // Release the press
        longPressSequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Perform the action
        driver.perform(Collections.singletonList(longPressSequence));
    }

    public void doubleTapElement(By locator) {
        WebElement element = driver.findElement(locator);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        // Create a sequence of actions for double-tap
        Sequence doubleTapSequence = new Sequence(finger, 0);

        // Move to the element location
        doubleTapSequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), element.getLocation().getX(), element.getLocation().getY()));

        // Perform the first tap
        doubleTapSequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTapSequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Add a small delay between the two taps (e.g., 200ms)
        doubleTapSequence.addAction(finger.createPointerMove(Duration.ofMillis(200), PointerInput.Origin.viewport(), 0, 0));

        // Perform the second tap
        doubleTapSequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTapSequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Perform the double tap action
        driver.perform(Collections.singletonList(doubleTapSequence));
    }

    public void typeText(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.sendKeys(text);
    }

    public void clearText(By locator) {
        WebElement element = driver.findElement(locator);
        element.clear();
    }

    public String getText(By locator) {
        WebElement element = driver.findElement(locator);
        return element.getText();
    }

    // Scroll to an element within the view (for long pages or lists).
    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    // swipeElement: For carousels, lists, sliders, or rearranging items in a list.
    public void swipeElement(WebElement element, int startX, int startY, int endX, int endY) {
        // Use PointerInput for touch actions
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        // Create the swipe sequence
        Sequence swipeSequence = new Sequence(finger, 0);

        // Move to the starting point (element location)
        swipeSequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));

        // Press at the starting point
        swipeSequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // Wait and move to the end point (simulate the swipe)
        swipeSequence.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY));

        // Release the finger after the swipe
        swipeSequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Perform the swipe action
        driver.perform(Collections.singletonList(swipeSequence));
    }

    /**
     * Scrolls the screen until the element located by the given locator is in view.
     * Uses the W3C Actions API for touch-based scrolling.
     *
     * @param locator The locator of the element to scroll to.
     * @throws NoSuchElementException if the element is not found.
     * @throws ElementNotInteractableException if the element is not interactable.
     */
    public void scrollToViewElement(By locator) throws NoSuchElementException, ElementNotInteractableException {
        WebElement element = driver.findElement(locator);

        if (element == null) {
            throw new NoSuchElementException("Element not found: " + locator);
        }

        // Get the Y position of the element and screen height
        int elementY = element.getLocation().getY();
        int screenHeight = driver.manage().window().getSize().getHeight();

        // Use W3C Actions API for scrolling
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 0);

        // Scroll from bottom of the screen to the element's Y position
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 0, screenHeight - 100));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // Move to the element's Y position
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), 0, elementY));

        // Release the touch action
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Perform the scroll action
        try {
            driver.perform(Collections.singletonList(scroll));
        } catch (Exception e) {
            throw new ElementNotInteractableException("Unable to scroll to element: " + locator, e);
        }
    }

// Swipe up or down on the screen for pagination or navigation.
public void swipeUp() {
    // Get the screen size
    Dimension size = driver.manage().window().getSize();
    int startX = size.width / 2;
    int startY = (int) (size.height * 0.8);
    int endY = (int) (size.height * 0.2);

    // Use PointerInput for touch actions (simulating a swipe up gesture)
    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

    // Create a new swipe sequence
    Sequence swipeSequence = new Sequence(finger, 0);

    // Move to the starting point (startX, startY)
    swipeSequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));

    // Press the screen at the starting point
    swipeSequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

    // Move to the end point (startX, endY) simulating the swipe action
    swipeSequence.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), startX, endY));

    // Release the touch at the end point
    swipeSequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

    // Perform the swipe action
    driver.perform(Collections.singletonList(swipeSequence));
}

    public void swipeDown() {
        // Get the screen size
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;  // Start in the middle of the screen horizontally
        int startY = (int) (size.height * 0.2);  // Start towards the top of the screen
        int endY = (int) (size.height * 0.8);    // End towards the bottom of the screen

        // Use PointerInput for touch actions (simulating a swipe down gesture)
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        // Create a new swipe sequence
        Sequence swipeSequence = new Sequence(finger, 0);

        // Move to the starting point (startX, startY)
        swipeSequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));

        // Press the screen at the starting point
        swipeSequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // Move to the end point (startX, endY) simulating the swipe down action
        swipeSequence.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), startX, endY));

        // Release the touch at the end point
        swipeSequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Perform the swipe action
        driver.perform(Collections.singletonList(swipeSequence));
    }


    /**
     * Waits for the element located by the given locator to be visible, with a customizable timeout.
     * The wait will stop early if the element becomes visible before the timeout.
     *
     * @param locator         The locator of the element to wait for.
     * @param timeoutInSeconds The maximum time to wait for the element to be visible in seconds.
     * @throws NoSuchElementException if the element is not found within the timeout period.
     */
    public void waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

        try {
            // Wait until the element is visible, but it will return early if the element becomes visible before the timeout
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            throw new NoSuchElementException("Element not visible: " + locator);
        }
    }

    /**
     * Waits for the element located by the given locator to be clickable, with a customizable timeout.
     * The wait will stop early if the element becomes clickable before the timeout.
     *
     * @param locator         The locator of the element to wait for.
     * @param timeoutInSeconds The maximum time to wait for the element to be clickable in seconds.
     * @throws NoSuchElementException if the element is not found within the timeout period.
     */
    public void waitForElementToBeClickable(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

        try {
            // Wait until the element is clickable, but it will return early if the element becomes clickable before the timeout
            wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            throw new NoSuchElementException("Element not clickable: " + locator);
        }
    }

    // Other reusable methods (e.g., click, sendKeys, etc.)
}
