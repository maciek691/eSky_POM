package pl.esky.pages.HomePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pl.esky.other.FileInput;
import pl.esky.pages.TestComponents.BaseTest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class tc02_CheckNewsletterWithIncorrectEmail extends BaseTest {

    private static final Logger LOG = LogManager.getLogger(tc02_CheckNewsletterWithIncorrectEmail.class);
    By domShadow = By.id("usercentrics-root");

    @Test
    public void tc02_CheckNewsletterWithIncorrectEmail(Method method) throws IOException {

        FileInput d = new FileInput();
        ArrayList<String> incorrectEmailsList = d.getData("IncorrectEmails");
        LOG.info("Array list created from the file");

        driver = initializeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(prop.getProperty("MainUrl"));
        HomePage homePage = new HomePage(driver);
        acceptCookies(domShadow);
        for (int i=1; i<incorrectEmailsList.size(); i++) {
            homePage.emailNewsletter.clear();
            homePage.emailNewsletter.sendKeys(incorrectEmailsList.get(i));
            homePage.signUpForNewsletter();
            //INFO: to catch error message in case if sth go wrong other than error message.
            try {
                Assert.assertEquals(homePage.newsletterErrorMessage.getText(), "Wpisz poprawny e-mail", "Error message is not appearing");
            } catch (Exception e) {
                LOG.error("there is error for test data: "+ incorrectEmailsList.get(i) + " -> " + e.getMessage());
                getScreenShotAndReturnPath(method.getName(), driver);
                Assert.fail("there is error for test data: "+ incorrectEmailsList.get(i) + " -> " + e.getMessage());
            }
            LOG.info("for incorrect address e-mail: " + incorrectEmailsList.get(i) + " error message is: " + homePage.newsletterErrorMessage.getText());
        }
    }
}
