package pl.esky.pages.HomePage;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.esky.Base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

public class  playground extends Base {

    private static final Logger log = LogManager.getLogger(TC01_linksSection.class);
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

    @Test(skipFailedInvocations = true)
    public void tekeScreenshot(Method method) throws IOException {
        driver.get(prop.getProperty("MainUrl"));
        HomePage hp = new HomePage(driver);
        hp.acceptCookies();
        //otwarcie nowej zakładki
        driver.switchTo().newWindow(WindowType.TAB);
        //utworzenie listy zakładek otwartych
        Set<String> tabOpened = driver.getWindowHandles();
        //przejście do konkretnej zakładki
        Iterator<String> iterator = tabOpened.iterator();
        String parentWindow = iterator.next();
        String childWindow = iterator.next();
        driver.switchTo().window(childWindow);
        //przejście do zakładci do którą stworzyliśmy w wcześniej
        driver.get("https://www.google.com/");
        //screenshot strony
        getScreenShotPath(method.getName(), driver);
        //powrót do poprzedniej zakładki
        driver.switchTo().window(parentWindow);
        //kolejne kroki testu

        int buttonHeight = hp.searchButton.getRect().getDimension().getHeight();
        int buttonWidth = hp.searchButton.getRect().getDimension().getWidth();
        log.info("height: "+ buttonHeight + " width: " + buttonWidth);
        System.out.println("height: "+ buttonHeight + " width: " + buttonWidth);
        hp.tripOneWayRadiobutton.click();
        hp.departureIfOneWay.sendKeys("AbuDabi");
        //Screensot jednego elementu
        File file = hp.departureIfOneWay.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File("screenshots/"+method.getName()+"_element.png"));
    }

    @Test
    public void StreamTest() {

        //to samo można robić z Listą
        int[] numbers = {11,22,32,14,11, 5,16,70,8,9,10};
        int sum = 0;
        System.out.println("sort");
        Arrays.stream(numbers).sorted().forEach(s-> System.out.println(s));
        System.out.println("multiply by 2");
        Arrays.stream(numbers).map(s->s*2).forEach(s-> System.out.println(s));
        System.out.println("sum");
        sum = Arrays.stream(numbers).sum();
        System.out.println(sum);
        // create Array from sorted stream
        int[] sortedNumbers = Arrays.stream(numbers).sorted().toArray();
        System.out.println(sortedNumbers[0]+", "+sortedNumbers[1]+", "+sortedNumbers[2]+", "+sortedNumbers[3]+", "+sortedNumbers[4]+", "+sortedNumbers[5]+", "+sortedNumbers[6]+", "+sortedNumbers[7]+", "+sortedNumbers[8]+", "+sortedNumbers[9]);
        System.out.println("convert to list of strings, and add 'number:' to it");
        /**  '::' To operator referencji do metody lub konstruktora.
            Zapis String[]::new jest skróconą i bardziej zwięzłą formą inicjalizacji nowej tablicy typu String[]
            W skrócie, wyrażenie String[]::new jest używane do tworzenia tablicy wynikowej, do której zostaną zebrane przekształcone elementy strumienia.
         **/
        String[] stringNumbers = Arrays.stream(numbers).mapToObj(s->"number: "+s).toArray(String[]::new);
        System.out.println(stringNumbers[0]+", "+stringNumbers[1]+", "+stringNumbers[2]+", "+stringNumbers[3]+", "+stringNumbers[4]+", "+stringNumbers[5]+", "+stringNumbers[6]+", "+stringNumbers[7]+", "+stringNumbers[8]+", "+stringNumbers[9]);
        List<String> listNumbers = new ArrayList<>(List.of(Arrays.toString(numbers)));
        System.out.println(listNumbers.size());
        boolean is32 = listNumbers.stream().anyMatch(s->s.contains("32"));
        System.out.println(is32);
        System.out.println("Zapisz nową tabelę z unikalnymi posortowanymi wartościmi tabeli numbers");
        int[] newNumbers = Arrays.stream(numbers).distinct().sorted().toArray();
        for (int num: newNumbers) {
            System.out.println(num);
        }
    }

    @DataProvider
    public Object[][] getData() {
        //[no of combination}][no of data]
        // login1, password1
        // login2, password2
        // login3, password3
        Object [][] data = new Object[3][2];
        data[0][0] = "login1";
        data[0][1] = "password1";
        data[1][0] = "login2";
        data[1][1] = "password2";
        data[2][0] = "login3";
        data[2][1] = "password3";
        return data;
    }

    @Test(dataProvider = "getData")
    public void dataProvider(String login, String password) {
        System.out.println("login: " + login + " password: " + password);
        log.info("login: " + login + " password: " + password);

    }
}
