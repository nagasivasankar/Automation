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
import mobileutilities.BaseClass;
import mobileutilities.ConfigFileUtility;
import mobileutilities.Utilities;
import pageobjects.HowIPreferToWorkPageObjects;
import pageobjects.LoginScreenPageObjects;

public class HowIPreferToWorkTest extends BaseClass {
	// create variable for extent reports
	ExtentReports reports;
	// create variable for extent test
	ExtentTest test;
	// create variable for Itest results
	ITestResult result;
	// creating object logger class
	Logger logger = Logger.getLogger(HowIPreferToWorkTest.class);
	// create object for MyProfilePageObjects class
	HowIPreferToWorkPageObjects hipo = new HowIPreferToWorkPageObjects(d);
	// create object for LoginScreenPageObjects class
	LoginScreenPageObjects lspo = new LoginScreenPageObjects(d);
	// create object for configgifleutility class
	ConfigFileUtility cfru = new ConfigFileUtility();
	// create variable version as integer
	int version = 10;
	// new DesiredCapabilities();
	DesiredCapabilities caps;

	// constructor
	HowIPreferToWorkTest() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		cfru.loadPropertyFile();
	}
	/*
	 * public static AndroidDriver<MobileElement> driver; public static
	 * AppiumDriverLocalService service;
	 */

	@BeforeSuite
	public void startServer() throws Exception {

		appiumServerStart(cfru.getProperty("appiumServerPath"), cfru.getProperty("appiumJsPath"));
	}

	@BeforeClass
	public void launchApplication() throws Exception {
		service.start();
		reports = new ExtentReports(reportFilePath+Utilities.date()+"_"+  "How I Prefer to Work.html", true);
		launchMobileApp(version);
	}
	@BeforeMethod
	public void login()
	{
		String email;
		test = reports.startTest("Verify How I Prefer to Work Screen Functionality");
		logger.info("open screen");
		email = lspo.login(d, cfru.getProperty("email"));
		logger.info("Enter vaild email address");
		test.log(LogStatus.INFO, "Email Details filled with : " + email);
		logger.info("Enter vaild password");
		test.log(LogStatus.INFO, "password Details filled");
		logger.info("Login button clicked");
		test.log(LogStatus.INFO, "Login button clicked");
		test.log(LogStatus.INFO, "More button clicked");
		test.log(LogStatus.INFO, "Work Preference button clicked");
		test.log(LogStatus.PASS, "Verified How I Prefer to work Screen navigation");
		reports.endTest(test);
	}
	@Test
	public void howIPreferToWork() {
		hipo.howIPrefertoWork(d);
		String pagetitle=hipo.verifyPageTitle(d);
		if(pagetitle.contains("My Work Preferences"))
		{
			test.log(LogStatus.INFO, "Page title: "+pagetitle);
			test.log(LogStatus.PASS, "Verified page title");			
		}
		else
		{
			test.log(LogStatus.INFO, "Page title : " +pagetitle);
			test.log(LogStatus.FAIL, "Verified page title");
		}
		String title=hipo.verifyText(d);
		test.log(LogStatus.INFO, "Text : "+title);
		test.log(LogStatus.PASS, "Verified text");
		String footertext=hipo.verifyFooterContent(d);
		test.log(LogStatus.INFO, "Footer Content  : "+footertext);
		test.log(LogStatus.PASS, "Verified footer content near toggle");
		String[] screensht=hipo.toggle(d);		
		logger.info("Toogle button clicked");
		for(String scrfilename:screensht)
			test.log(LogStatus.INFO, "Screenshot taken : "+scrfilename);
		test.log(LogStatus.PASS, "Toggle button clicked");
		test.log(LogStatus.INFO, "save button clicked");
		test.log(LogStatus.PASS, "How I Prefer work test passed");
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