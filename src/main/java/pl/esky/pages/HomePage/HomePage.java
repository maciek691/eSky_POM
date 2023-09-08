package pl.esky.pages.HomePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class HomePage {

    static final String E_SKY_URL_MAIN = "https://www.esky.pl/";
    private static final Logger log = LogManager.getLogger(HomePage.class);
    // Variables
    private final WebDriver driver;
    // Locators
    @FindBy(id = "CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll")
    WebElement acceptCookiesButton;
    @FindBy(id = "TripTypeOneway")
    WebElement tripOneWayRadiobutton;
    @FindBy(id = "serviceClass")
    WebElement tripClass;
    @FindBy(id = "departureOneway")
    WebElement departureIfOneWay;
    @FindBy(id = "arrivalOneway")
    WebElement arrivalIfOneWay;
    @FindBy(id = "departureDateOneway")
    WebElement departureDateIfOneWay;
    @FindBy(className = "ui-datepicker-month")
    WebElement month;
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-circle-triangle-e']")
    WebElement plusMonth;
    @FindBy(id = "adultPax")
    WebElement adultPassengers;
    @FindBy(id = "childPax")
    WebElement childPassengers;
    @FindBy(css = "div[data-pax-type='adult'] [class='plus']")
    WebElement plusAdult;
    @FindBy(css = "div[data-pax-type='child'] [class='plus']")
    WebElement plusChild;
    @FindBy(xpath = "//fieldset[@class='trip-search']/button")
    WebElement searchButton;
    @FindBy(xpath = "//div[contains(@class,'col')]/ul/li/a")
    WebElement linksInLinksSection;
    @FindBy(id = "email")
    WebElement emailNewsletter;
    @FindBy(id = "submit")
    WebElement submitNewsletter;
    @FindBy(css = "em[class='error-msg']")
    WebElement newsletterErrorMessage;


    // Constructor
    public HomePage(final WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Actions

    public void acceptCookies() {
        acceptCookiesButton.click();
    }

    public void setOneWayTrip() {
        tripOneWayRadiobutton.click();
    }

    public void setTripClass(String journeyClass) {
        Select journey = new Select(tripClass);
        journey.selectByValue(journeyClass);
    }

    public void setDepartureIfOneWay(String departureCity) {
        departureIfOneWay.sendKeys(departureCity);
    }

    public void setArrivalCity(String arrivalCity) {
        arrivalIfOneWay.sendKeys(arrivalCity);
    }

    public void setMonth(String lookingMonth) {
        departureDateIfOneWay.click();
        while (!month.getText().contains(lookingMonth)) {
            plusMonth.click();
        }
    }

    public void setDay(String lookingDay) {
        int numberOfDays = driver.findElements(By.cssSelector("td[data-handler='selectDay']")).size();

        for (int i = 0; i < numberOfDays; i++) {
            String day = driver.findElements(By.className("ui-state-default")).get(i).getText();
            if (day.equalsIgnoreCase(lookingDay)) {
                driver.findElements(By.className("ui-state-default")).get(i).click();
                break;
            }
        }
    }

    public void setAdultPassengers(String numberOfAdultPassengers) {
        while (!adultPassengers.getAttribute("value").equalsIgnoreCase(numberOfAdultPassengers)) {
            plusAdult.click();
        }
    }

    public void setNumberOfChildPassengers(String numberOfChildPassengers) {
        while (!childPassengers.getAttribute("value").equalsIgnoreCase(numberOfChildPassengers)) {
            plusChild.click();
        }
    }

    public void search() {
        searchButton.click();
    }

    public void checkLinksInLinksSection(String xpathListLokator) throws IOException {
        SoftAssert softAssert = new SoftAssert();

        List<WebElement> links = driver.findElements(By.xpath(xpathListLokator));
        String prywatnoscLink = "Prywatność"; // as only this link use js.
        for (WebElement link : links) {
            try {
                String url = link.getAttribute("href");
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("HEAD");
                connection.connect();
                int respondCode = connection.getResponseCode();

                if (respondCode < 400) {
                    log.info("the link " + link.getText() + " is ok");
                } else {
                    log.error("the link " + link.getText() + " is broke with code " + respondCode);
                }

                softAssert.assertTrue(respondCode < 400, "The link with text " + link.getText() + " is broke with code "
                        + respondCode);

            } catch (MalformedURLException e) {
                Assert.assertEquals(prywatnoscLink, link.getText());
                log.warn("The link with text " + link.getText() + " throw Exception " + e.getMessage());
                System.out.println("The link with text " + link.getText() + " throw Exception " + e.getMessage());
            }
        }
        softAssert.assertAll();
    }

    public void signUpForNewsletter() {
        submitNewsletter.click();
    }

}
