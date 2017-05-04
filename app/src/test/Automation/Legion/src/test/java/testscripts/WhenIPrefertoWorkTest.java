package testscripts;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import mobileutilities.ConfigFileUtility;
import mobileutilities.Utilities;
import mobileutilities.BaseClass.Retry;
import pageobjects.LoginScreenPageObjects;
import pageobjects.MyProfilePageObjects;
import pageobjects.WhenIPrefertoWorkPageObjects;
import pageobjects.WhenImBusyPageObjects;

public class WhenIPrefertoWorkTest extends mobileutilities.BaseClass {
	// create variable for extent reports
	ExtentReports reports;
	// create variable for extent test
	ExtentTest test;
	// create variable for Itest results
	ITestResult result;
	// creating object logger class
	Logger logger = Logger.getLogger(WhenIPrefertoWorkTest.class);
	// create object for MyProfilePageObjects class
	WhenIPrefertoWorkPageObjects wipg = new WhenIPrefertoWorkPageObjects(d);
	// create object for LoginScreenPageObjects class
	LoginScreenPageObjects lspo = new LoginScreenPageObjects(d);
	// create onject for configfileutility class
	ConfigFileUtility cfru = new ConfigFileUtility();
	// create variable version as integer
	int version = 10;
	public String email = "sam.ford@legion.co";

	// constructor
	WhenIPrefertoWorkTest() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		cfru.loadPropertyFile();
	}

	@BeforeSuite
	public void startServer() {
		appiumServerStart(cfru.getProperty("appiumServerPath"), cfru.getProperty("appiumJsPath"));
	}
	@BeforeMethod
	public void login()
	{
		String email;
		test = reports.startTest("Verify When I Prefer to Work Screen Functionality");
		logger.info("open screen");
		email = lspo.login(d, cfru.getProperty("email"));
		logger.info("Enter vaild email address");
		test.log(LogStatus.INFO, "Email Details filled with : " + email);
		logger.info("Enter vaild password");
		test.log(LogStatus.INFO, "password Details filled");
		logger.info("Login button clicked");
		test.log(LogStatus.INFO, "Login button clicked");
		test.log(LogStatus.INFO, "More button clicked");
		test.log(LogStatus.INFO, "Avaliablity button clicked");
		test.log(LogStatus.PASS, "Verified when I Prefer to work Screen navigation");
		reports.endTest(test);
	}
	@BeforeClass
	public void launchApplication() throws Exception {
		service.start();
		reports = new ExtentReports(reportFilePath +Utilities.date()+"_"+ "WhenIPrefertoWork.html", true);
		launchMobileApp(version);
	}

	@Test(retryAnalyzer = Retry.class)
	public void whenIPreferToWork() {
		String scrfilename=wipg.whenIPreferToWork(d);
		test.log(LogStatus.INFO, "ScreenShot taken : "+scrfilename);
		String pagetitle=wipg.verifyPageTitle(d);
		if(pagetitle.contains("My Availability")){
			test.log(LogStatus.INFO, "Page Title : "+pagetitle);
			test.log(LogStatus.PASS, "Verified page title");
		}
		else{
			test.log(LogStatus.INFO, "Page Title : "+pagetitle);
			test.log(LogStatus.FAIL, "Verified page title");
		}
		
		String hourstext=wipg.verifyhourstext(d);
		test.log(LogStatus.INFO, "Footer content : "+hourstext);
		test.log(LogStatus.PASS, "Verified footer content");
		String weekDay=wipg.verifyWeekDay(d);
		test.log(LogStatus.INFO, "Week Day : "+weekDay);
		test.log(LogStatus.PASS, "Verified Week Day");
		test.log(LogStatus.PASS, "When I Prefer To Work Test passed");
		reports.endTest(test);
		lspo.logout(d);
	}

	@AfterMethod
	public void result(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(LogStatus.FAIL, "Test failed");
		}
		reports.endTest(test);
		reports.flush();
	}

	@AfterClass
	public void teardown() {

		// closing app
		reports.endTest(test);
		reports.flush();
		d.closeApp();
		service.stop();
		System.out.println("current status:" + service.isRunning());
	}

}
