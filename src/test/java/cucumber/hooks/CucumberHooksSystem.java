package cucumber.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import pl.esky.pages.TestComponents.BaseTest;

import java.util.concurrent.TimeUnit;

public class CucumberHooksSystem extends BaseTest {
    @Before
    public void BeforeDisplayMessage(Scenario sc) {
        System.out.println("Start test " + sc.getName());
    }

    @After
    public void AfterDisplayMessage(Scenario sc) {
        System.out.println("End test: " + sc.getName());
        driver.quit();
    }
}
