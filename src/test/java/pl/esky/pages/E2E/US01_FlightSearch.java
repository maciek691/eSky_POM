package pl.esky.pages.E2E;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.esky.Base;
import pl.esky.pages.SearchingFlightResultPage.SearchingFlightResultPage;
import pl.esky.pages.HomePage.HomePage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class US01_FlightSearch extends Base {
        /* As a Client
        I want to find a flight for me and my family
        from Warsaw Chopin Airport to Cracow Balice Airport  */

    WebDriver driver;
    private static Logger log = LogManager.getLogger(US01_FlightSearch.class);
    public Properties prop = new Properties();

    @BeforeTest (groups = {"E2E"})
    public void setup() throws IOException {
        driver = initializeDriver();

        FileInputStream fis = new FileInputStream("src/main/resources/data.properties");
        prop.load(fis);
    }

    @AfterTest (groups = {"E2E"})
    public void end() {
        driver.quit();
    }

    @Test (groups = {"E2E"})
    public void searchFlight() throws InterruptedException, IOException {
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
        log.info("browser: " + browser);
        HomePage homePage = new HomePage(driver);
        homePage.acceptCookies();
        log.info("cookies accepted");
        homePage.setOneWayTrip();
        log.info("One way trip ok");
//        flightSearch.setTripClass(journeyClass);
//        log.info("Class trip ok");
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
        Assert.assertEquals(searchingFlightResultPage.getTitle(), "Warszawa (PL) → Kraków (PL) - Wyniki wyszukiwania");
        Assert.assertEquals(searchingFlightResultPage.isOffersListDisplayed(), true);
        log.info("Landing Page ok");
    }

    @Test
    public void test() {
        System.out.println("test excluded from group");
    }

    @Test
    public void test2() {
        System.out.println("test excluded from group");
    }

    @Test (groups = {"E2E"})
    public void test3() {
        System.out.println("test included to group");
        log.info("test3 successfully included to group");
    }


}
