package pl.esky.pages.TestComponents;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class BaseTest {

    public static WebDriver driver;
    public Properties prop;
    public static String browser;
    public static By acceptCookiesButton = By.cssSelector("button[data-testid='uc-accept-all-button']");
    private static final Logger LOG = LogManager.getLogger(BaseTest.class);;
    WebElement domShadow;
    String browserName;
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public String getBrowserName() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/data.properties");
        prop.load(fis);
        browserName = prop.getProperty("browser").toLowerCase();
        return browserName;
    }
    public WebDriver initializeDriver() throws IOException {
        switch(getBrowserName()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                System.setProperty("webdriver.http.factory", "jdk-http-client");
                driver = new ChromeDriver();
                browser = "Chrome";
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                System.setProperty("webdriver.http.factory", "jdk-http-client");
                driver = new FirefoxDriver();
                browser = "FireFox";
                break;
            case "safari":
//             WebDriverManager.safaridriver().setup();
                System.setProperty("webdriver.http.factory", "jdk-http-client");
                driver = new SafariDriver();
                browser = "Safari";
                break;
//         case "opera":
//             System.setProperty("webdriver.opera.driver", "src/main/java/pl/esky/pages/Resources/Drivers/operadriver");
//             driver = new OperaDriver();
//             break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                System.setProperty("webdriver.http.factory", "jdk-http-client");
                driver = new EdgeDriver();
                browser = "Edge";
                break;
            default:
                LOG.error("Invalid browser name: " + getBrowserName());
                throw new IllegalStateException("Invalid browser name: " + getBrowserName());
        }
        return driver;
    }

    public String getScreenShotPath(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destinationFile = "screenshots/" + testCaseName + " " + dateFormat.format(date) + ".png";
        FileUtils.copyFile(source, new File(destinationFile));
        return destinationFile;
    }

    public static void acceptCookies(By id) throws IOException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        if (browser == "Chrome" || browser == "Edge") {
            WebElement shadowHost = driver.findElement(id);
            SearchContext shadowRoot = shadowHost.getShadowRoot();
            WebElement button = wait.until(ExpectedConditions.visibilityOf(shadowRoot.findElement(acceptCookiesButton)));
            shadowRoot.findElement(acceptCookiesButton).click();
        } else {
            WebElement shadowHost = driver.findElement(id);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            List<?> elements = (List<?>) js.executeScript("return arguments[0].shadowRoot.children", shadowHost);
            WebElement shadowRoot = (WebElement) elements.get(0);
            WebElement button = wait.until(ExpectedConditions.visibilityOf(shadowRoot.findElement(acceptCookiesButton)));
            shadowRoot.findElement(acceptCookiesButton).click();
        }
    }
}
