package pl.esky;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Base {

    public WebDriver driver;

    public Properties prop;
    public static String browser;
    String browserName;
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final Logger LOG = LogManager.getLogger(Base.class);

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

}
