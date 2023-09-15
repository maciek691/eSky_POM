package pl.esky.other;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReporterNG {
    //External html report

    static ExtentReports extent;

    public static ExtentReports getReportObject() {
        SimpleDateFormat reportNameDateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date reportDate = new Date();
        String reportPath = System.getProperty("user.dir")+"/reports/report "+reportNameDateFormat.format(reportDate) +".html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
        reporter.config().setReportName("E-sky Test Automation Report");
        reporter.config().setDocumentTitle("Test Results");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester", "MW");
        return extent;
    }
}
