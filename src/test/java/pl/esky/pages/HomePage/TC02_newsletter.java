package pl.esky.pages.HomePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.esky.Base;
import pl.esky.other.FileInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TC02_newsletter extends Base {

    private static Logger log = LogManager.getLogger(TC02_newsletter.class);
    WebDriver driver;

    @BeforeTest
    public void setup() throws IOException {
        driver = initializeDriver();
        driver.get(prop.getProperty("MainUrl"));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        log.info("browser: " + prop.getProperty("browser"));
    }


    @AfterTest
    public void end() {
        driver.quit();
    }

    @Test
     public void checkNewsletterWithIncorrectEmail() throws IOException {
        FileInput d = new FileInput();
        ArrayList<String> incorrectEmailsList = d.getData("IncorrectEmails");
        log.info("Array list created from the file");

        HomePage homePage = new HomePage(driver);
        homePage.acceptCookies();
        for (int i=1; i<incorrectEmailsList.size(); i++) {
            homePage.emailNewsletter.clear();
            homePage.emailNewsletter.sendKeys(incorrectEmailsList.get(i));
            homePage.signUpForNewsletter();
            Assert.assertEquals(homePage.newsletterErrorMessage.getText(), "Wpisz poprawny e-mail");
            log.info("for incorrect address e-mail: " + incorrectEmailsList.get(i) + " error message is: " + homePage.newsletterErrorMessage.getText());
        }
    }
}
