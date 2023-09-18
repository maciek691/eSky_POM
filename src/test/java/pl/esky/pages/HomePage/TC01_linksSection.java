package pl.esky.pages.HomePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.esky.Base;

import java.io.IOException;
import java.time.Duration;

public class TC01_linksSection extends Base {

    private static Logger log = LogManager.getLogger(TC01_linksSection.class);
    WebDriver driver;

    String xpathToLinksInLinksSection = "//div[contains(@class,'col')]/ul/li/a";

    @BeforeTest
    public void setup() throws IOException {
        driver = initializeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        log.info("browser: " + prop.getProperty("browser"));
    }

    @AfterTest
    public void end() {
        driver.quit();
    }

    @Test
    public void checkLinksInLinksSection() throws IOException, InterruptedException {
        HomePage homePage = new HomePage(driver);
        driver.get(prop.getProperty("MainUrl"));
//        Thread.sleep(5000);
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usercentrics-root")));
        homePage.acceptCookies();
        homePage.checkLinksInLinksSection(xpathToLinksInLinksSection);
    }
}
