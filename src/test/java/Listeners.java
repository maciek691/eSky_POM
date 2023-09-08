import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import pl.esky.Base;
import pl.esky.other.ExtentReporterNG;

import java.io.IOException;

public class Listeners extends Base implements ITestListener {

    ExtentTest test;
    ExtentReports extent = ExtentReporterNG.getReportObject();
    // to allowed parallel tests run
    ThreadLocal <ExtentTest> extentTest = new ThreadLocal<ExtentTest>();

    @Override
    public void onTestStart(ITestResult iTestResult) {
        test = extent.createTest(iTestResult.getMethod().getMethodName());
        // set test variable for parallel tests run
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        extentTest.get().log(Status.PASS,"Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        extentTest.get().fail(iTestResult.getThrowable());
        WebDriver driver = null;
        String testMethodName = iTestResult.getMethod().getMethodName();

        try {
            driver = (WebDriver)iTestResult.getTestClass().getRealClass().getDeclaredField("driver").get(iTestResult.getInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            extentTest.get().addScreenCaptureFromPath(getScreenShotPath(testMethodName, driver), iTestResult.getMethod().getMethodName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        extent.flush();
    }
}
