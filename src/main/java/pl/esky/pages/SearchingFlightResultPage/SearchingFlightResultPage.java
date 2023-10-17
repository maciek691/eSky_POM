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

    public String searchResultPageTitle = "Warszawa (PL) → Kraków (PL) - Wyniki wyszukiwania";
    // Locators
    @FindBy(css = "ul[class='offers']")
    WebElement offersList;


    //Actions

    public boolean isOffersListDisplayed() {
    	return offersList.isDisplayed();
    }
}

