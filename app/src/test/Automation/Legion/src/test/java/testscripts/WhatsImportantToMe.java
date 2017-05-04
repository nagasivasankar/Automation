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
import mobileutilities.ConfigFileUtility;
import mobileutilities.Utilities;
import mobileutilities.BaseClass.Retry;
import pageobjects.LoginScreenPageObjects;
import pageobjects.WhatsImportantToMePageObjects;

public class WhatsImportantToMe extends mobileutilities.BaseClass {
	// create variable for extent reports
	ExtentReports reports;
	// create variable for extent test
	ExtentTest test;
	// create variable for Itest results
	ITestResult result;
	// creating object logger class
	Logger logger = Logger.getLogger(WhatsImportantToMe.class);
	// create object for MyProfilePageObjects class
	WhatsImportantToMePageObjects mppg = new WhatsImportantToMePageObjects(d);
	// create object for LoginScreenPageObjects class
	LoginScreenPageObjects lspo = new LoginScreenPageObjects(d);
	// create object for configfileutility class
	ConfigFileUtility cfru = new ConfigFileUtility();
	// create variable version as integer
	int version = 10;

	// constructor
	WhatsImportantToMe() throws Exception {
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
		reports = new ExtentReports(reportFilePath +Utilities.date()+"_"+"whatsImpoartanttome.html", true);
		launchMobileApp(version);
	}
	@BeforeMethod
	public void login()
	{
		String email;
		test = reports.startTest("Verify What's Important to me Screen Functionality");
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
		test.log(LogStatus.INFO,"What's Important to me button clicked");
		test.log(LogStatus.PASS, "Verified what's important to me Screen navigation");
		reports.endTest(test);
	}
	@Test(retryAnalyzer = Retry.class)
	public void whatsImpoartantToMe() {
		mppg.myPerferencesSave(d);
		String pagetitle=mppg.verifyPageTitle(d);
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
		String title=mppg.verifytext(d);
		test.log(LogStatus.INFO, "Text : "+title);
		test.log(LogStatus.PASS, "Verified text");
		String chxboxtext=mppg.checkboxtext(d);
		test.log(LogStatus.INFO, "Checkbox Text : "+chxboxtext);
		test.log(LogStatus.PASS, "Verified check box title");
		logger.info("Clicked Check Box");
		String[] scrfilename = mppg.checkboxClick(d);
		for(String screensht:scrfilename)
			test.log(LogStatus.INFO, "Screenshot taken : " +screensht);
		test.log(LogStatus.PASS,"Verified clicking checkbox");
		test.log(LogStatus.PASS, "What's Important to me test passed");
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
		d.closeApp();
		reports.endTest(test);
		reports.flush();
		service.stop();
		System.out.println("current status:" + service.isRunning());
	}
}