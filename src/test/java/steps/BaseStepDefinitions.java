package steps;

import io.cucumber.java.en.Given;
import org.testng.Assert;  // Use TestNG Assert
import utils.CommonActions;

public class BaseStepDefinitions {

    private final CommonActions commonActions = new CommonActions();

    // Common step to launch the app
    @Given("I open the app")
    public void iOpenTheApp() {
    // Opening app is handled by the app launch
    }

    // Common utility methods like clicking back button, closing keyboard, etc.
    @Given("I close the keyboard")
    public void iCloseTheKeyboard() {
        // Assuming you have a method for closing the keyboard
        commonActions.closeKeyboard();
    }

    @Given("I click the back button")
    public void iClickBackButton() {
        // Assuming you have a method for clicking the back button
        commonActions.clickBackButton();
    }

    // Add more common methods like `launchApp`, `logout`, etc.
}
