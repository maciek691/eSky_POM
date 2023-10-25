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
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class BaseTest {

    public static WebDriver driver;
    public Properties prop;
    public static String browser;
    public static By acceptCookiesButton = By.cssSelector("button[data-testid='uc-accept-all-button']");
    private static final Logger LOG = LogManager.getLogger(BaseTest.class);
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
        browser = getBrowserName();
        switch(browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default:
                LOG.error("Invalid browser name: " + getBrowserName());
                throw new IllegalStateException("Invalid browser name: " + getBrowserName());
        }
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        LOG.info("driver for " + browser + " initialized");
        return driver;
    }

    public String getScreenShotAndReturnPath(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destinationFile = "screenshots/" + testCaseName + " " + dateFormat.format(date) + ".png";
        FileUtils.copyFile(source, new File(destinationFile));
        return destinationFile;
    }

    public void acceptCookies(By id) throws IOException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        browser = getBrowserName();
        WebElement shadowHost = driver.findElement(id);
        if (Objects.equals(browser, "chrome") || Objects.equals(browser, "edge")) {
            SearchContext shadowRoot = shadowHost.getShadowRoot();
            WebElement button = wait.until(ExpectedConditions.visibilityOf(shadowRoot.findElement(acceptCookiesButton)));
            shadowRoot.findElement(acceptCookiesButton).click();
        } else {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            List<?> elements = (List<?>) js.executeScript("return arguments[0].shadowRoot.children", shadowHost);
            WebElement shadowRoot = (WebElement) elements.get(0);
            WebElement button = wait.until(ExpectedConditions.visibilityOf(shadowRoot.findElement(acceptCookiesButton)));
            shadowRoot.findElement(acceptCookiesButton).click();
        }
        LOG.info("Cookies accepted");
    }

    @BeforeTest
    public void setup() throws IOException {

//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    }

    @AfterTest
    public void end() {
        driver.quit();
    }
}
