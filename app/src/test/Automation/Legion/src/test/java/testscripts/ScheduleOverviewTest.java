package testscripts;

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

import mobileutilities.ConfigFileUtility;
import mobileutilities.Utilities;
import mobileutilities.BaseClass.Retry;
import pageobjects.LoginScreenPageObjects;
import pageobjects.ScheduleOverviewPageObjects;

public class ScheduleOverviewTest extends mobileutilities.BaseClass {
	// create variable for extent reports
	ExtentReports reports;
	// create variable for extent test
	ExtentTest test;
	// create variable for Itest results
	ITestResult result;
	// creating object gor logger class
	Logger logger = Logger.getLogger(ScheduleOverviewTest.class);
	// create object for ScheduleOverviewPageObjects class
	ScheduleOverviewPageObjects sopg = new ScheduleOverviewPageObjects(d);

	// create object for LoginScreenPageObjects class
	LoginScreenPageObjects lspo = new LoginScreenPageObjects(d);
	ConfigFileUtility cfru = new ConfigFileUtility();
	// create variable version as integer
	int version = 10;

	// constructor
	ScheduleOverviewTest() throws Exception {
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

		reports = new ExtentReports(reportFilePath + "scheduleoverview.html"+"_"+Utilities.date(), true);
		test = reports.startTest("verify the Legion App Launching");
		launchMobileApp(version);
		test.log(LogStatus.INFO, "App launched");
		test.log(LogStatus.INFO, "Application is up and running");
		test.log(LogStatus.PASS, "verified login screen launched");
		reports.endTest(test);

	}
	@BeforeMethod
	public void login()
	{
		String email;
		test = reports.startTest("Verify ScheduleOverview Functionality");
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

	@Test(retryAnalyzer = Retry.class)
	public void ScheduleOverview() {
		sopg.pastScheduleNavigation(d);
		 test.log(LogStatus.PASS, "Verified Navigation to pastSchedule" );
		 sopg.testSummary(d);
		 test.log(LogStatus.PASS, "Verified Summary details in pastSchedule" );
		 sopg.testTitle(d);
		 test.log(LogStatus.PASS, "Verified getting title name in pastSchedule" );
		 sopg.testScheduleTitle(d);
		 test.log(LogStatus.PASS, "Verified getting Schedule title name in pastSchedule" );
		 sopg.testScheduleText(d);
		 test.log(LogStatus.PASS, "Verified approval of text getting in pastSchedule" );
		 sopg.testScheduleTime(d);
		 test.log(LogStatus.PASS, "Verified time interval in pastSchedule" );
		 sopg.testScheduleAddress(d);
		 test.log(LogStatus.PASS, "Verified getting address  in pastSchedule" );
		 sopg.testStoreLead(d);
		 test.log(LogStatus.PASS, "Verified StoreLead  in pastSchedule" );
		 sopg.testCalling(d);
		 test.log(LogStatus.PASS, "Verified Calling  in pastSchedule" );
		 sopg.testDirections(d);
		 test.log(LogStatus.PASS, "Verified directions  in pastSchedule" );
		 sopg.testletsStarted(d);
		 test.log(LogStatus.PASS, "Verified lets started  in pastSchedule" );
		 sopg.testSchedule(d);
		 test.log(LogStatus.PASS, "Verified schedule button in pastSchedule" );
		 sopg.testScheduleTile(d);
		 test.log(LogStatus.PASS, "Verified  week tile name in pastSchedule" );
		 sopg.testSchedulePrevNavigation(d);
		 test.log(LogStatus.PASS, "Verified navigation to previous in pastSchedule" );
		 sopg.testScheduleTitleName(d);
		 test.log(LogStatus.PASS, "Verified title name in pastSchedule" );
		 sopg.testScheduleBackNavigation(d);
		 test.log(LogStatus.PASS, "Verified navigation to back in pastSchedule" );
		
	     sopg.currentScheduleNavigation(d);
		 test.log(LogStatus.PASS, "Verified Navigation to currentSchedule" );
		 sopg.testSummary(d);
		 test.log(LogStatus.PASS, "Verified Summary details in currentSchedule" );
		 sopg.testTitle(d);
		 test.log(LogStatus.PASS, "Verified getting title name in currentSchedule" );
		 sopg.testScheduleTitle(d);
		 test.log(LogStatus.PASS, "Verified getting Schedule title name in currentSchedule" );
		 sopg.testScheduleText(d);
		 test.log(LogStatus.PASS, "Verified approval of text getting in currentSchedule" );
		 sopg.testScheduleTime(d);
		 test.log(LogStatus.PASS, "Verified time interval in currentSchedule" );
		 sopg.testScheduleAddress(d);
		 test.log(LogStatus.PASS, "Verified getting address  in currentSchedule" );
		 sopg.shiftOffer(d);
		 test.log(LogStatus.PASS, "Verified Shift offer in currentSchedule" );
		 sopg.shiftCancel(d);
		 test.log(LogStatus.PASS, "Verified Shift cancel in currentSchedule" );
		 sopg. testScheduleBackNavigation(d);
		 test.log(LogStatus.PASS, "Verified navigation to back  in currentSchedule" );
		 sopg.testSchedule(d);
		 test.log(LogStatus.PASS, "Verified schedule button in currentSchedule" );
		 sopg.testScheduleTile(d);
		 test.log(LogStatus.PASS, "Verified  week tile name in currentSchedule" );
		 sopg.testSchedulePrevNavigation(d);
		 test.log(LogStatus.PASS, "Verified navigation to previous in currentSchedule" );
		 sopg.testScheduleTitleName(d);
		 test.log(LogStatus.PASS, "Verified title name in currentSchedule" );
		 sopg.testScheduleBackNavigation(d);
		 test.log(LogStatus.PASS, "Verified navigation to back in currentSchedule" );
		 test.log(LogStatus.PASS, "Verified currentSchedule  Functionality" );
		
		 sopg.finalizedScheduleNavigation(d);
		 test.log(LogStatus.PASS, "Verified Navigation to finalizedSchedule" );
		 sopg.testSummary(d);
		 test.log(LogStatus.PASS, "Verified Summary details in finalizedSchedule" );
		 sopg.testTitle(d);
		 test.log(LogStatus.PASS, "Verified getting title name in finalizedSchedule" );
		 sopg.testScheduleTitle(d);
		 test.log(LogStatus.PASS, "Verified getting Schedule title name in finalizedSchedule" );
		 sopg.testScheduleText(d);
		 test.log(LogStatus.PASS, "Verified approval of text getting in finalizedSchedule" );
		 sopg.testScheduleTime(d);
		 test.log(LogStatus.PASS, "Verified time interval in finalizedSchedule" );
		 sopg.testScheduleAddress(d);
		 test.log(LogStatus.PASS, "Verified getting address  in finalizedSchedule" );
		 sopg.shiftOffer(d);
		 test.log(LogStatus.PASS, "Verified Shift offer in finalizedSchedule" );
		 sopg.shiftCancel(d);
		 test.log(LogStatus.PASS, "Verified Shift cancel in finalizedSchedule" );
		 sopg. testScheduleBackNavigation(d);
		 test.log(LogStatus.PASS, "Verified Shift cancel in finalizedSchedule" );
	     sopg.testSchedule(d);
		 test.log(LogStatus.PASS, "Verified schedule button in finalizedSchedule" );
		 sopg.testScheduleTile(d);
		 test.log(LogStatus.PASS, "Verified  week tile name in finalizedSchedule" );
		 sopg.testSchedulePrevNavigation(d);
		 test.log(LogStatus.PASS, "Verified navigation to previous in finalizedSchedule" );
		 sopg.testScheduleTitleName(d);
		 test.log(LogStatus.PASS, "Verified title name in finalizedSchedule" );
		 sopg.testScheduleBackNavigation(d);
		 test.log(LogStatus.PASS, "Verified navigation to back in finalizedSchedule" );
		 test.log(LogStatus.PASS, "Verified  finalizedSchedule Functionality" );
	
        sopg.publishedScheduleNavigation(d);
		 test.log(LogStatus.PASS, "Verified Navigation to publishedSchedule" );
		 sopg.testSummary(d);
		 test.log(LogStatus.PASS, "Verified Summary details in publishedSchedule" );
		 sopg.testTitle(d);
		 test.log(LogStatus.PASS, "Verified getting title name in publishedSchedule" );
		 sopg.testScheduleTitle(d);
		 test.log(LogStatus.PASS, "Verified getting Schedule title name in publishedSchedule" );
		 sopg.testScheduleText(d);
		 test.log(LogStatus.PASS, "Verified approval of text getting in publishedSchedule" );
		 sopg.testScheduleTime(d);
		 test.log(LogStatus.PASS, "Verified time interval in publishedSchedule" );
		 sopg.testScheduleAddress(d);
		 test.log(LogStatus.PASS, "Verified getting address  in publishedSchedule" );
		 sopg.shiftOffer(d);
		 test.log(LogStatus.PASS, "Verified Shift offer in publishedSchedule" );
		 sopg.shiftCancel(d);
		 test.log(LogStatus.PASS, "Verified Shift cancel in publishedSchedule" );
		 sopg. testScheduleBackNavigation(d);
		 test.log(LogStatus.PASS, "Verified Shift cancel in publishedSchedule" );
		 sopg.testSchedule(d);
		 test.log(LogStatus.PASS, "Verified schedule button in publishedSchedule" );
		 sopg.testScheduleTile(d);
		 test.log(LogStatus.PASS, "Verified  week tile name in publishedSchedule" );
		 sopg.testSchedulePrevNavigation(d);
		 test.log(LogStatus.PASS, "Verified navigation to previous in publishedSchedule" );
		 sopg.testScheduleTitleName(d);
		 test.log(LogStatus.PASS, "Verified title name in publishedSchedule" );
		 sopg.testScheduleBackNavigation(d);
		 test.log(LogStatus.PASS, "Verified navigation to back in publishedSchedule" );
         test.log(LogStatus.PASS, "Verified  publishedSchedule Functionality" );
         
		lspo.logout(d);
		test.log(LogStatus.PASS, "ScheduleOverview test passed");
		reports.endTest(test);
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
	public void tearDown() {
		d.closeApp();
		reports.endTest(test);
		reports.flush();
		service.stop();
		System.out.println("current status:" + service.isRunning());
	}

}
