package pl.esky.pages.cucumberStepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pl.esky.pages.HomePage.HomePage;
import pl.esky.pages.TestComponents.BaseTest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StepDefinitions extends BaseTest {

    By domShadow = By.id("usercentrics-root");
    WebDriver driver = initializeDriver();
    HomePage homePage = new HomePage(driver);


    public StepDefinitions() throws IOException {
    }

    @Given("I am landed on homepage")
    public void i_am_landed_on_homepage() throws IOException {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(prop.getProperty("MainUrl"));
        acceptCookies(domShadow);
    }

    @Given("I am on the newsletter section")
    public void i_am_on_the_newsletter_section() throws IOException {
        homePage.emailNewsletter.clear();
    }

    @When("^type my (.+) address, check to box with special offers and click subscribe$")
    public void type_my_address_check_to_box_with_special_offers_and_click_subscribe(String email) {
        homePage.emailNewsletter.sendKeys(email);
        homePage.checkbox.get(3).click();
        homePage.signUpForNewsletter();
    }
    @Then("I should see a success message")
    public void i_should_see_a_success_message() throws InterruptedException {
        Assert.assertEquals((homePage.successNewsletterMessageText).getText(), "Otrzymujesz już newsletter", "Message is not appearing");
    }
}
