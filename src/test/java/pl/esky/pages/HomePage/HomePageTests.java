package pl.esky.pages.HomePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import pl.esky.other.FileInput;
import pl.esky.pages.SearchingFlightResultPage.SearchingFlightResultPage;
import pl.esky.pages.TestComponents.BaseTest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class HomePageTests extends BaseTest {
    WebDriver driver;
    private static final Logger LOG = LogManager.getLogger(HomePageTests.class);
    //    By id = By.id("usercentrics-root");
    @BeforeMethod(groups = {"E2E"})
    public void setup() throws IOException {
        driver = initializeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        LOG.info("browser: " + browser);
    }

    @AfterMethod(groups = {"E2E"})
    public void end() {
        driver.quit();
    }

    @Test(groups = {"E2E"})
    public void us01_SearchFlight() throws InterruptedException, IOException {

        /* As a Client
        I want to find a flight for me and my family
        from Warsaw Chopin Airport to Cracow Balice Airport  */

        SoftAssert softAssert = new SoftAssert();

        //testing data
        String journeyClass = "business";
        String departureCity = "Warszawa";
        String arrivalCity = "Kraków";
        String lookingMonth = "Luty";
        String lookingDay = "10";
        String numberOfAdultPassengers = "2";
        String numberOfChildPassengers = "2";
        By oneWayArrivalCity = By.id("arrivalOneway");
        By oneWayDepartureCity = By.id("departureOneway");
        By domShadow = By.id("usercentrics-root");

        driver.get(prop.getProperty("MainUrl"));

        HomePage homePage = new HomePage(driver);
        acceptCookies(domShadow);
        LOG.info("cookies accepted");
        homePage.setOneWayTrip();
        LOG.info("One way trip ok");
//        flightSearch.setTripClass(journeyClass);
//        log.info("Class trip ok");
//        homePage.setDepartureIfOneWay(departureCity);
////        Thread.sleep(1000);
//        log.info("Departure City set");
//
//        homePage.setArrivalCity(arrivalCity);
////        Thread.sleep(1000);
////        driver.findElement(By.id("arrivalOneway")).sendKeys(Keys.ENTER);
//        log.info("Arrival City set");

        homePage.setDepartureIfOneWay(departureCity);
        Thread.sleep(1000);
        // to choose Warszawa Chopin airport from the list
        for (int i = 0; i < 2; i++) {
            driver.findElement(oneWayDepartureCity).sendKeys(Keys.ARROW_DOWN);
        }
        driver.findElement(oneWayDepartureCity).sendKeys(Keys.ENTER);
        LOG.info("Departure City set");
        homePage.setArrivalCity(arrivalCity);
        Thread.sleep(1000);
        driver.findElement(oneWayArrivalCity).sendKeys(Keys.ENTER);
        LOG.info("Arrival City set");
        homePage.setMonth(lookingMonth);
        LOG.info("Month set");
        homePage.setDay(lookingDay);
        LOG.info("Day set");
        homePage.setAdultPassengers(numberOfAdultPassengers);
        LOG.info("Adult passengers set");
        homePage.setNumberOfChildPassengers(numberOfChildPassengers);
        LOG.info("Children passengers set");
        SearchingFlightResultPage searchingFlightResultPage = homePage.search();
        Thread.sleep(3000);
        softAssert.assertEquals(driver.getTitle(), searchingFlightResultPage.searchResultPageTitle, "Title mismatch");
        softAssert.assertTrue(searchingFlightResultPage.isOffersListDisplayed(),  "Offers list is not displayed");
        try {
            softAssert.assertAll();
        }
        catch (AssertionError e) {
            LOG.error("Assertion error: " + e.getMessage());
        }
        finally {
            softAssert.assertAll();
        }
        LOG.info("Landing Page ok");
    }

    @Test
    public void tc01_CheckLinksInSectionLinks() throws IOException, InterruptedException {
        HomePage homePage = new HomePage(driver);
        driver.get(prop.getProperty("MainUrl"));
        acceptCookies(By.id("usercentrics-root"));
        homePage.checkLinksInLinksSection(homePage.xpathToLinksInLinksSection);
    }

    @Test
    public void tc02_CheckNewsletterWithIncorrectEmail(Method method) throws IOException {

        FileInput d = new FileInput();
        ArrayList<String> incorrectEmailsList = d.getData("IncorrectEmails");
        LOG.info("Array list created from the file");

        HomePage homePage = new HomePage(driver);
        driver.get(prop.getProperty("MainUrl"));
        acceptCookies(By.id("usercentrics-root"));
        for (int i=1; i<incorrectEmailsList.size(); i++) {
            homePage.emailNewsletter.clear();
            homePage.emailNewsletter.sendKeys(incorrectEmailsList.get(i));
            homePage.signUpForNewsletter();
            //INFO: to catch error message in case if sth go wrong other than error message.
            try {
                Assert.assertEquals(homePage.newsletterErrorMessage.getText(), "Wpisz poprawny e-mail", "Error message is not appearing");
            } catch (Exception e) {
                LOG.error("there is error for test data: "+ incorrectEmailsList.get(i) + " -> " + e.getMessage());
                getScreenShotPath(method.getName(), driver);
                Assert.fail("there is error for test data: "+ incorrectEmailsList.get(i) + " -> " + e.getMessage());
            }
            LOG.info("for incorrect address e-mail: " + incorrectEmailsList.get(i) + " error message is: " + homePage.newsletterErrorMessage.getText());
        }
    }
}
