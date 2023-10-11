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

import static pl.esky.Base.browser;
import static pl.esky.Base.isLinkBroken;


public class HomePage {

    private static final Logger log = LogManager.getLogger(HomePage.class);
    // Variables
    private final WebDriver driver;
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

    public By calendar = By.cssSelector("td[data-handler='selectDay']");
    public By xpathToLinksInLinksSection = By.xpath("//div[contains(@class,'col')]/ul/li/a");
    public By acceptCookiesButton = By.cssSelector("button[data-testid='uc-accept-all-button']");

    // Constructor
    public HomePage(final WebDriver driver) throws IOException {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

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

    public void setDay(By calendar, String lookingDay) {
        List<WebElement> days = driver.findElements(calendar);
        days.stream().filter(day -> day.getText().equalsIgnoreCase(lookingDay)).findFirst().get().click();


//        int numberOfDays = driver.findElements(By.cssSelector("td[data-handler='selectDay']")).size();
//
//        for (int i = 0; i < numberOfDays; i++) {
//            String day = driver.findElements(By.className("ui-state-default")).get(i).getText();
//            if (day.equalsIgnoreCase(lookingDay)) {
//                driver.findElements(By.className("ui-state-default")).get(i).click();
//                break;
//            }
//        }
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

//    public void checkLinksInLinksSection(String xpathListLokator) throws IOException {
//        SoftAssert softAssert = new SoftAssert();
//
//        List<WebElement> links = driver.findElements(By.xpath(xpathListLokator));
//        String prywatnoscLink = "Prywatność"; // as only this link use js.
//
//        for (WebElement link : links) {
//            try {
//                String url = link.getAttribute("href");
//                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//                connection.setRequestMethod("HEAD");
//                connection.connect();
//                int respondCode = connection.getResponseCode();
//
//                if (respondCode < 400) {
//                    log.info("the link " + link.getText() + " is ok");
//                } else {
//                    log.error("the link " + link.getText() + " is broke with code " + respondCode);
//                }
//
//                softAssert.assertTrue(respondCode < 400, "The link with text " + link.getText() + " is broke with code "
//                        + respondCode);
//
//            } catch (MalformedURLException e) {
//                Assert.assertEquals(prywatnoscLink, link.getText());
//                log.warn("The link with text " + link.getText() + " throw Exception " + e.getMessage());
//                    System.out.println("The link with text " + link.getText() + " throw Exception " + e.getMessage());
//            }
//        }
//        softAssert.assertAll();
//    }

    public void checkLinksInLinksSection(By xpathListLokator) {
        // Create list of the links
        List<WebElement> links = driver.findElements(xpathListLokator);
        //check if the links are not broken
        List<WebElement> brokenLinks = links.stream().filter(link -> isLinkBroken(link)).collect(Collectors.toList());
        //log the broken links
        log.warn("There are " + brokenLinks.size() + " broken links");
        for (WebElement brokenLink : brokenLinks) {
            log.error("the link " + brokenLink.getText() + " is broke");
        }
        Assert.assertTrue(brokenLinks.isEmpty(), "There are " + brokenLinks.size() + " broken links");
    }

    public void signUpForNewsletter() {
        submitNewsletter.click();
    }

}
