package cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/test/java/cucumber"},
        glue = {"pl/esky/pages/cucumberStepDefinitions", "cucumber/hooks"},
//        tags = "@newsletter",
        monochrome = true,
        plugin = {"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
//                "pretty", "html:reports/cucumberReport.html"
        })

public class TestNGCucumberRunner extends AbstractTestNGCucumberTests {

}
