package pl.esky.pages.HomePage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.annotations.*;
import pl.esky.General;
import pl.esky.pages.TestComponents.BaseTest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class playground extends BaseTest {

    private static final Logger log = LogManager.getLogger(playground.class);

    String xpathToLinksInLinksSection = "//div[contains(@class,'col')]/ul/li/a";

    //additional timeout pof this specific test
    @Test(timeOut = 4000)
    public void test() {
        System.out.println("test excluded from group");
        log.info("test1 excluded to group");
    }

    /**
     * enabled = false wyłącza całkowicie dany test,
     * skipFailedInvocations = false dotyczy testów parametrycznych i kontroluje,
     * czy TestNG powinien kontynuować wykonywanie zestawów danych po napotkaniu niepowodzenia w jednym z nich.
     */
    @Test(enabled = false)
    public void test2() {
        System.out.println("test excluded from group");
        log.info("test2 excluded from group");
    }

    @Test(groups = {"includeGroup"})
    public void test3() {
        System.out.println("test included to group");
        log.info("test3 included to group");
    }

    @Test(groups = {"includeGroup"},skipFailedInvocations = true)
    @Parameters({"parameter1", "parameter2", "parameter3"})
    public void test4(String param1, String param2, String param3) {
        System.out.println(param1);
        log.info(param1);
        System.out.println(param2);
        log.info(param2);
        System.out.println(param3);
        log.info(param3);
    }

    @Test(timeOut = 4000)
    public void test5() {
        System.out.println("test excluded from group");
        log.info("test1 excluded to group");
    }

    @Test(retryAnalyzer = pl.esky.pages.TestComponents.Retry.class)
    public void tekeScreenshot(Method method) throws IOException {
        driver = initializeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(prop.getProperty("MainUrl"));
        HomePage hp = new HomePage(driver);
        acceptCookies(By.id("usercentrics-root"));
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
        getScreenShotAndReturnPath(method.getName(), driver);
        //powrót do poprzedniej zakładki
        driver.switchTo().window(parentWindow);
        //kolejne kroki testu

        int buttonHeight = hp.searchButton.getRect().getDimension().getHeight();
        int buttonWidth = hp.searchButton.getRect().getDimension().getWidth();
        log.info("height: " + buttonHeight + " width: " + buttonWidth);
        System.out.println("height: " + buttonHeight + " width: " + buttonWidth);
        hp.tripOneWayRadiobutton.click();
        hp.departureIfOneWay.sendKeys("AbuDabi");
        //Screensot jednego elementu
        File file = hp.departureIfOneWay.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File("screenshots/" + method.getName() + "_element.png"));
    }

    @Test
    public void StreamTest() {

        //to samo można robić z Listą
        int[] numbers = {11, 22, 32, 14, 11, 5, 16, 70, 8, 9, 10};
        int sum = 0;
        System.out.println("sort");
        Arrays.stream(numbers).sorted().forEach(s -> System.out.println(s));
        System.out.println("multiply by 2");
        Arrays.stream(numbers).map(s -> s * 2).forEach(s -> System.out.println(s));
        System.out.println("sum");
        sum = Arrays.stream(numbers).sum();
        System.out.println(sum);
        // create Array from sorted stream
        int[] sortedNumbers = Arrays.stream(numbers).sorted().toArray();
        System.out.println(sortedNumbers[0] + ", " + sortedNumbers[1] + ", " + sortedNumbers[2] + ", " + sortedNumbers[3] + ", " + sortedNumbers[4] + ", " + sortedNumbers[5] + ", " + sortedNumbers[6] + ", " + sortedNumbers[7] + ", " + sortedNumbers[8] + ", " + sortedNumbers[9]);
        System.out.println("convert to list of strings, and add 'number:' to it");
        /**  '::' To operator referencji do metody lub konstruktora.
         Zapis String[]::new jest skróconą i bardziej zwięzłą formą inicjalizacji nowej tablicy typu String[]
         W skrócie, wyrażenie String[]::new jest używane do tworzenia tablicy wynikowej, do której zostaną zebrane przekształcone elementy strumienia.
         **/
        String[] stringNumbers = Arrays.stream(numbers).mapToObj(s -> "number: " + s).toArray(String[]::new);
        System.out.println(stringNumbers[0] + ", " + stringNumbers[1] + ", " + stringNumbers[2] + ", " + stringNumbers[3] + ", " + stringNumbers[4] + ", " + stringNumbers[5] + ", " + stringNumbers[6] + ", " + stringNumbers[7] + ", " + stringNumbers[8] + ", " + stringNumbers[9]);
        List<String> listNumbers = new ArrayList<>(List.of(Arrays.toString(numbers)));
        System.out.println(listNumbers.size());
        boolean is32 = listNumbers.stream().anyMatch(s -> s.contains("32"));
        System.out.println(is32);
        System.out.println("Zapisz nową tabelę z unikalnymi posortowanymi wartościmi tabeli numbers");
        int[] newNumbers = Arrays.stream(numbers).distinct().sorted().toArray();
        for (int num : newNumbers) {
            System.out.println(num);
        }
    }

    @DataProvider
    public Object[][] getData() {
        //[no of combination}][no of data]
        // login1, password1
        // login2, password2
        // login3, password3
        Object[][] data = new Object[3][2];
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

    @DataProvider
    public Object[][] getData2() {
        return new Object[][]{{"login1", "password1"}, {"login2", "password2"}, {"login3", "password3"}};
    }

    @Test(dataProvider = "getData2")
    public void dataProvider2(String login, String password) {
        System.out.println("login: " + login + " password: " + password);
        log.info("login: " + login + " password: " + password);
    }

    @DataProvider
    public Object[][] getData3() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("login", "login1");
        map.put("password", "password1");
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("login", "login2");
        map2.put("password", "password2");
        HashMap<String, String> map3 = new HashMap<String, String>();
        map3.put("login", "login3");
        map3.put("password", "password3");
        return new Object[][]{{map}, {map2}, {map3}};
    }

    @Test(dataProvider = "getData3")
    public void dataProvider3(HashMap<String, String> data) {
        System.out.println("login: " + data.get("login") + ", password: " + data.get("password"));
        log.info("login: " + "login: " + data.get("login") + ", password: " + data.get("password"));
    }


    //Function to upload the json file as hash map
    public List<HashMap<String, String>> getDataFromJsonFile(String filePath) throws IOException {
        //json to string
        String jsonContent = FileUtils.readFileToString(new File(filePath), "UTF-8");
        //string to HashMap - Jackson Databind
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, String>> jsonData = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {
        });
        return jsonData;
    }

    String pathJsonFileWithData = "src/test/java/pl/esky/pages/testData/dataPlaygroundJson.json";

    //    String pathJsonFileWithData = System.getProperty("user.dir")+"src//main//resources//dataPlaygroundJson.json";
    @DataProvider
    public Object[][] getData4() throws IOException {
        List<HashMap<String, String>> data = getDataFromJsonFile(pathJsonFileWithData);
        return new Object[][]{{data.get(0)}, {data.get(1)}, {data.get(2)}}; //data.get(0) - pierwszy element z listy
    }

    @Test(dataProvider = "getData4")
    public void dataProvider4(HashMap<String, String> data) {
        System.out.println("login: " + data.get("login") + ", password: " + data.get("password"));
        log.info("login: " + "login: " + data.get("login") + ", password: " + data.get("password"));
    }
}
