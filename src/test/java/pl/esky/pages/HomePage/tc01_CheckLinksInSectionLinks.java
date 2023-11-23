package pl.esky.pages.HomePage;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import pl.esky.pages.TestComponents.BaseTest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class tc01_CheckLinksInSectionLinks extends BaseTest {
    By domShadow = By.id("usercentrics-root");

    @Test
    public void tc01_CheckLinksInSectionLinks() throws IOException, InterruptedException {
        driver = initializeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(prop.getProperty("MainUrl"));
        HomePage homePage = new HomePage(driver);
        acceptCookies(domShadow);
        homePage.checkLinksInLinksSection(homePage.xpathToLinksInLinksSection);

    }
}
