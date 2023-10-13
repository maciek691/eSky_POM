package pl.esky.pages.E2E;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;
import pl.esky.Base;
import pl.esky.pages.SearchingFlightResultPage.SearchingFlightResultPage;
import pl.esky.pages.HomePage.HomePage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class US01_FlightSearch extends Base {
        /* As a Client
        I want to find a flight for me and my family
        from Warsaw Chopin Airport to Cracow Balice Airport  */

    WebDriver driver;
    private static final Logger log = LogManager.getLogger(US01_FlightSearch.class);

    @BeforeTest (groups = {"E2E"})
    public void setup() throws IOException {
        driver = initializeDriver();
        log.info("browser: " + browser);
    }

    @AfterTest (groups = {"E2E"})
    public void end() {
        driver.quit();
    }

    @Test (groups = {"E2E"})
    public void searchFlight() throws InterruptedException, IOException {
        SoftAssert softAssert = new SoftAssert();

        //testing data
        String journeyClass = "business";
        String departureCity = "Warszawa";
        String arrivalCity = "Kraków";
        String lookingMonth = "Luty";
        String lookingDay = "10";
        String numberOfAdultPassengers = "2";
        String numberOfChildPassengers = "2";


        driver.get(prop.getProperty("MainUrl"));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        HomePage homePage = new HomePage(driver);
        homePage.acceptCookies();
        log.info("cookies accepted");
        homePage.setOneWayTrip();
        log.info("One way trip ok");
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
            driver.findElement(By.id("departureOneway")).sendKeys(Keys.ARROW_DOWN);
        }
        driver.findElement(By.id("departureOneway")).sendKeys(Keys.ENTER);
        log.info("Departure City set");

        homePage.setArrivalCity(arrivalCity);
        Thread.sleep(1000);
        driver.findElement(By.id("arrivalOneway")).sendKeys(Keys.ENTER);
        log.info("Arrival City set");

        homePage.setMonth(lookingMonth);
        log.info("Month set");
        homePage.setDay(lookingDay);
        log.info("Day set");
        homePage.setAdultPassengers(numberOfAdultPassengers);
        log.info("Adult passengers set");
        homePage.setNumberOfChildPassengers(numberOfChildPassengers);
        log.info("Children passengers set");
        homePage.search();

        SearchingFlightResultPage searchingFlightResultPage = new SearchingFlightResultPage(driver);
        Thread.sleep(3000);
        softAssert.assertEquals(searchingFlightResultPage.getTitle(), "Warszawa (PL) → Kraków (PL) - Wyniki wyszukiwania", "Title mismatch");
        softAssert.assertTrue(searchingFlightResultPage.isOffersListDisplayed(),  "Offers list is not displayed");
        try {
            softAssert.assertAll();
        }
        catch (AssertionError e) {
            log.error("Assertion error: " + e.getMessage());
        }
        finally {
            softAssert.assertAll();
        }
        log.info("Landing Page ok");
    }

    //additional timeout pof this specific test
    @Test (timeOut = 4000)
    public void test() {
        System.out.println("test excluded from group");
        log.info("test1 excluded to group");
    }
    /** enabled = false wyłącza całkowicie dany test,
     * skipFailedInvocations = false dotyczy testów parametrycznych i kontroluje,
     * czy TestNG powinien kontynuować wykonywanie zestawów danych po napotkaniu niepowodzenia w jednym z nich. */
    @Test (enabled = false, skipFailedInvocations = false)
    public void test2() {
        System.out.println("test excluded from group");
        log.info("test2 excluded to group");
    }

    @Test (groups = {"E2E"})
    public void test3() {
        System.out.println("test included to group");
        log.info("test3 included to group");
    }
    @Test(groups = {"E2E"})
    @Parameters ({"parameter1", "parameter2", "parameter3"})
        public void test4(String param1, String param2, String param3) {
        System.out.println(param1);
        log.info(param1);
        System.out.println(param2);
        log.info(param2);
        System.out.println(param3);
        log.info(param3);
    }

    @Test (timeOut = 4000)
    public void test5() {
        System.out.println("test excluded from group");
        log.info("test1 excluded to group");
    }
}
