package pl.esky;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class General {

    public WebDriver driver;

    public General (WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public Properties prop;
    public static String browser;
    String browserName;
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final Logger LOG = LogManager.getLogger(General.class);



    public static boolean isLinkBroken(WebElement link) {
        try {
            String url = link.getAttribute("href");
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            int responseCode = connection.getResponseCode();
            return responseCode != 200;
        } catch (MalformedURLException e) {
            if (e.getMessage().equals("unknown protocol: javascript")) {
                LOG.warn("The link with text " + link.getText() + " throw Exception " + e.getMessage());
                return false;
            } else {
                LOG.error("The link with text " + link.getText() + " throw Exception " + e.getMessage());
                return true;
            }
        } catch (IOException e) {
            System.out.println("The link with text " + link.getText() + " throw Exception " + e.getMessage());
            LOG.error("The link with text " + link.getText() + " throw Exception " + e.getMessage());
            return true;
        }
    }

    public void waitForElement (WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated((By) webElement));
    }

    public String getTitleOfThePage() {
        String pageTitle = driver.getTitle();
        return pageTitle;
    }
 }
