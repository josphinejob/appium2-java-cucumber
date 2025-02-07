package steps;

import pages.HomePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
public class HomePageSteps {
    private final HomePage homePage = new HomePage();

    @When("I navigate to the home page")
    public void i_navigate_to_the_home_page() {
        //homepage opened by app launch
    }

    @Then("I should see the {string}")
    public void i_should_see_the(String elementName) {
        homePage.verifyElementIsDisplayed(elementName);
    }
}
