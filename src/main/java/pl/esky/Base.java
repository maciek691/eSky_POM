package pl.esky;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Base {

    public WebDriver driver;
    public Properties prop;

    public WebDriver initializeDriver() throws IOException {
     prop = new Properties();
     FileInputStream fis = new FileInputStream("src/main/resources/data.properties");
     prop.load(fis);
     String browserName = prop.getProperty("browser");




     switch(browserName) {
         case "Chrome":
             System.setProperty("webdriver.chrome.driver", "src/main/resources/Drivers/chromedriver");
             System.setProperty("webdriver.http.factory", "jdk-http-client");
             driver = new ChromeDriver();
             break;
         case "FireFox":
             System.setProperty("webdriver.gecko.driver", "src/main/resources/Drivers/geckodriver");
             System.setProperty("webdriver.http.factory", "jdk-http-client");
             driver = new FirefoxDriver();
             break;
         case "Safari":
             System.setProperty("webdriver.http.factory", "jdk-http-client");
             driver = new SafariDriver();
             break;
//         case "Opera":
//             System.setProperty("webdriver.opera.driver", "src/main/java/pl/esky/pages/Resources/Drivers/operadriver");
//             driver = new OperaDriver();
//             break;
         case "Edge":
             System.setProperty("webdriver.edge.driver", "src/main/resources/Drivers/msedgedriver");
             System.setProperty("webdriver.http.factory", "jdk-http-client");
             driver = new EdgeDriver();
             break;
         default:
             System.out.println("please check 'browser' property");
     }
        return driver;
    }

    public String getScreenShotPath(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destinationFile = System.getProperty("user.dir")+"/reports/"+testCaseName+".png";
        FileUtils.copyFile(source, new File(destinationFile));
        return destinationFile;
    }

}
