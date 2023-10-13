package pl.esky.pages.HomePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.esky.General;

import java.io.IOException;
import java.time.Duration;

public class TC01_linksSection extends General {

    private static final Logger LOG = LogManager.getLogger(TC01_linksSection.class);
    WebDriver driver;



    @BeforeTest
    public void setup() throws IOException {
        driver = initializeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        LOG.info("browser: " + prop.getProperty("browser"));
    }

    @AfterTest
    public void end() {
        driver.quit();
    }

    @Test
    public void checkLinksInSectionLinks() throws IOException, InterruptedException {
        HomePage homePage = new HomePage(driver);
        driver.get(prop.getProperty("MainUrl"));
        homePage.acceptCookies();
        homePage.checkLinksInLinksSection(homePage.xpathToLinksInLinksSection);
    }
}
