package pl.esky.pages.HomePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static pl.esky.General.browser;
import static pl.esky.General.isLinkBroken;


public class HomePage {


    // Constructor
    private WebDriver driver;
    public HomePage(final WebDriver driver) throws IOException {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    // Variables
    private static final Logger LOG = LogManager.getLogger(HomePage.class);

//    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    // Locators
    @FindBy(id = "usercentrics-root")
    WebElement domShadow;
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
    @FindBy(css = "td[data-handler='selectDay']")
    List <WebElement> calendar;

    public By xpathToLinksInLinksSection = By.xpath("//div[contains(@class,'col')]/ul/li/a");
    public By acceptCookiesButton = By.cssSelector("button[data-testid='uc-accept-all-button']");


    // Actions

    public void acceptCookies() throws IOException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        if (browser == "Chrome" || browser == "Edge") {
            WebElement shadowHost = domShadow;
            SearchContext shadowRoot = shadowHost.getShadowRoot();
            WebElement button = wait.until(ExpectedConditions.visibilityOf(shadowRoot.findElement(acceptCookiesButton)));
            shadowRoot.findElement(acceptCookiesButton).click();
        } else {
            WebElement shadowHost = domShadow;
            JavascriptExecutor js = (JavascriptExecutor) driver;
            List<?> elements = (List<?>) js.executeScript("return arguments[0].shadowRoot.children", shadowHost);
            WebElement shadowRoot = (WebElement) elements.get(0);
            WebElement button = wait.until(ExpectedConditions.visibilityOf(shadowRoot.findElement(acceptCookiesButton)));
            shadowRoot.findElement(acceptCookiesButton).click();
        }
    }

    public void setOneWayTrip() {
        tripOneWayRadiobutton.click();
    }

    public void setTripClass(String journeyClass) {
        Select journey = new Select(tripClass);
        journey.selectByValue(journeyClass);
    }

//    public void setDepartureIfOneWay(String departureCity) throws InterruptedException {
//        Actions a = new Actions(driver);
//        a.sendKeys(departureIfOneWay, departureCity).build().perform();
//        Thread.sleep(3000);
////        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("autocomplete-holder")));
//        driver.findElement(By.cssSelector("a[data-city-code='waw']")).click();
//    }
//
//    public void setArrivalCity(String arrivalCity) throws InterruptedException {
//        Actions a = new Actions(driver);
//        a.sendKeys(arrivalIfOneWay, arrivalCity).build().perform();
//        Thread.sleep(3000);
////        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("autocomplete-holder")));
//        driver.findElement(By.cssSelector("a[data-city-code='KRK']")).click();
//    }
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
        List<WebElement> days = calendar;
        days.stream().filter(day -> day.getText().equalsIgnoreCase(lookingDay)).findFirst().get().click();
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

    public void checkLinksInLinksSection(By xpathListLokator) {
        // Create list of the links
        List<WebElement> links = driver.findElements(xpathListLokator);
        //check if the links are not broken
        List<WebElement> brokenLinks = links.stream().filter(link -> isLinkBroken(link)).collect(Collectors.toList());
        //log the broken links
        LOG.warn("There are " + brokenLinks.size() + " broken links");
        for (WebElement brokenLink : brokenLinks) {
            LOG.error("the link " + brokenLink.getText() + " is broke");
        }
        Assert.assertTrue(brokenLinks.isEmpty(), "There are " + brokenLinks.size() + " broken links");
    }

    public void signUpForNewsletter() {
        submitNewsletter.click();
    }

}
