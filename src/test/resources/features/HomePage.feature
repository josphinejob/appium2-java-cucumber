@home
Feature: Home Page Verification
  As a user, I want to verify the home page elements
  So that I can ensure the app is functioning correctly

  Scenario Outline: Verify home page elements
    Given I open the app
    When I navigate to the home page
    Then I should see the "<HomePageElement>"

    Examples:
      | HomePageElement |
      | Header          |
      | Text   |
