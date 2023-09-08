package pl.esky.pages.HomePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.esky.Base;

import java.io.IOException;

public class TC01_linksSection extends Base {

    private static Logger log = LogManager.getLogger(TC01_linksSection.class);
    WebDriver driver;

    String xpathToLinksInLinksSection = "//div[contains(@class,'col')]/ul/li/a";

    @BeforeTest
    public void setup() throws IOException {
        driver = initializeDriver();
        driver.get(prop.getProperty("MainUrl"));
        log.info("browser: " + prop.getProperty("browser"));
    }

    @AfterTest
    public void end() {
        driver.quit();
    }

    @Test
    public void checkLinksInLinksSection() throws IOException {
        HomePage homePage = new HomePage(driver);
        homePage.acceptCookies();
        homePage.checkLinksInLinksSection(xpathToLinksInLinksSection);
    }
}
