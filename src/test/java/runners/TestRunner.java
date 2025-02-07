package runners;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.AppiumDriverManager;

import java.net.MalformedURLException;

@CucumberOptions(
        features = "src/test/resources/features",  // Path to your feature files
        glue = {"steps","hooks"},                  // Package for step definitions and hooks
        plugin = {"pretty", "html:target/cucumber-reports/report.html", "json:target/cucumber-reports/cucumber.json"}  // Reporting options
)
public class TestRunner extends AbstractTestNGCucumberTests {
    @BeforeSuite
    public void appSetUp() throws MalformedURLException {
        System.out.println("Starting Appium Driver");
        AppiumDriverManager.startDriver();
    }

    @AfterSuite
    public void quitDriver() {
        AppiumDriverManager.quitDriver();
    }
}

