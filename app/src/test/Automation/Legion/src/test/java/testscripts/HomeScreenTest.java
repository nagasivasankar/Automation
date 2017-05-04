package testscripts;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import mobileutilities.ConfigFileUtility;
import mobileutilities.Utilities;
import pageobjects.HomeScreenPageObjects;
import pageobjects.LoginScreenPageObjects;

public class HomeScreenTest extends mobileutilities.BaseClass {

	// create variable for extent reports
	ExtentReports reports;
	// create variable for extent test
	ExtentTest test;
	// create variable for Itest results
	String result;
	// creating object logger logger class
	Logger logger = Logger.getLogger(HomeScreenTest.class);
	// create object for HomeScreenPageObjects class
	HomeScreenPageObjects hspo = new HomeScreenPageObjects(d);
	// create object for LoginScreenPageObjects class
	LoginScreenPageObjects lspo = new LoginScreenPageObjects(d);
	// create object for configfileutility class
	ConfigFileUtility cfru = new ConfigFileUtility();
	// create variable version as integer
	int version = 10;

	// constructor
	HomeScreenTest() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		cfru.loadPropertyFile();
	}

	@BeforeSuite
	public void startServer() {
		appiumServerStart(cfru.getProperty("appiumServerPath"), cfru.getProperty("appiumJsPath"));
	}

	@BeforeClass
	public void launchApplication() throws Exception {
		service.start();
		reports = new ExtentReports(reportFilePath +Utilities.date()+"_"+"Homescreen.html", true);
		launchMobileApp(version);
	}
	@BeforeMethod
	public void login()
	{
		String email;
		test = reports.startTest("Verify Home Screen Functionality");
		logger.info("open screen");
		email = lspo.login(d, cfru.getProperty("email"));
		logger.info("Enter vaild email address");
		test.log(LogStatus.INFO, "Email Details filled with : " + email);
		logger.info("Enter vaild password");
		test.log(LogStatus.INFO, "password Details filled");
		logger.info("Login button clicked");
		test.log(LogStatus.PASS, "Verified home screen navigation");
		reports.endTest(test);
	}
	@Test
	public void homeScreen() 
	{		
		String text[] =hspo.gettingText(d);
		String scrfilename1;
		scrfilename1=Utilities.captureScreenshot(d, "homeScreen");
		logger.info("Getting wishes");
		test.log(LogStatus.PASS, "Verified wishes text");
		String daytext=hspo.verifyDate(d);
		test.log(LogStatus.INFO, "Day : "+daytext);
		logger.info("Getting current day text");
		test.log(LogStatus.PASS, "Verified current day text");
		logger.info("Getting day");
		test.log(LogStatus.PASS, "Verified day text");
		logger.info("Getting shift pending");
		test.log(LogStatus.PASS, "Verified shift pending text");
		logger.info("Getting schedule, message, shift offers with count");
		test.log(LogStatus.PASS, "Verified schedule,shift offers with count"); 
		for (String gettingtext : text)
		test.log(LogStatus.INFO, "Verified text : "+gettingtext);
		String[] scrfilename=hspo.navigations(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +scrfilename1 );
		for (String screenshot : scrfilename)
		test.log(LogStatus.INFO, "Screenshot taken : "+screenshot);
		test.log(LogStatus.PASS, "Home Screen test passed");		
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
		d.closeApp();
		reports.endTest(test);
		reports.flush();
		service.stop();
		System.out.println("current status:" + service.isRunning());
	}
}
