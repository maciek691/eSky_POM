package pl.esky.pages.SearchingFlightResultPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchingFlightResultPage {

    // Variables
    private final WebDriver driver;

    //Constructor
    public SearchingFlightResultPage (final WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    // Locators
    @FindBy(css = "ul[class='offers']")
    WebElement offersList;


    //Actions

    public String getTitle () {
        String title = driver.getTitle();
        return title;
    }

    public boolean isOffersListDisplayed() {
    	return offersList.isDisplayed();
    }
}

