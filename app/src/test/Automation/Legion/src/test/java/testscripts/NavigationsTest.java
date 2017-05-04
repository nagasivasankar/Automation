package testscripts;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.JavascriptExecutor;
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
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import mobileutilities.ConfigFileUtility;
import mobileutilities.Utilities;
import mobileutilities.BaseClass.Retry;
import pageobjects.LoginScreenPageObjects;
import pageobjects.NavigationsPageObjects;

public class NavigationsTest extends mobileutilities.BaseClass {
	ExtentReports reports;
	ExtentTest test;
	String result;
	Logger logger = Logger.getLogger(NavigationsTest.class);
	NavigationsPageObjects npo = new NavigationsPageObjects(d);
	// create object for LoginScreenPageObjects class
	LoginScreenPageObjects lspo = new LoginScreenPageObjects(d);
	ConfigFileUtility cfru = new ConfigFileUtility();
	// Create variable version as integer to use in switch case for calling
	// Android version
	int version = 10;
	DesiredCapabilities caps;

	// Constructor and Load the Config file and log4j.
	NavigationsTest() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		cfru.loadPropertyFile();
	}

	@BeforeSuite
	public void startServer() {
		appiumServerStart(cfru.getProperty("appiumServerPath"), cfru.getProperty("appiumJsPath"));
	}

	@BeforeClass
	// Start the Appium server and Launch the app
	public void launchApplication() throws Exception {
		service.start();
		reports = new ExtentReports(reportFilePath + Utilities.date()+"_"+"Navigations.html", true);
		launchMobileApp(version);
	}
	@BeforeMethod
	public void login()
	{
		String email;
		test = reports.startTest("Verify navigations");
		logger.info("open screen");
		email = lspo.login(d, cfru.getProperty("email"));
		logger.info("Enter vaild email address");
		test.log(LogStatus.INFO, "Email Details filled with : " + email);
		logger.info("Enter vaild password");
		test.log(LogStatus.INFO, "password Details filled");
		logger.info("Login button clicked");
		test.log(LogStatus.INFO, "Login button clicked");
		test.log(LogStatus.PASS, "Verified home screen navigation");
		reports.endTest(test);
	}
	// App Navigation flow
	@Test(retryAnalyzer = Retry.class)
	public void navigationScreen()
	{
		String[] title=npo.verifyTitleText(d);
		for(String text:title)
			if(text.equals("0"))
				break;
			else
				test.log(LogStatus.INFO, "Page Title Text : "+text);
		String scrfilename = npo.verifyHome(d);
		String titlehome = npo.homeScreenTitle(d);
		test.log(LogStatus.INFO, "Home screen title : " +titlehome);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrfilename);
		test.log(LogStatus.PASS, "Home button clicked");
		String scrschedule = npo.verifySchedule(d);
		String titleschedule = npo.scheduleScreenTitle(d);
		test.log(LogStatus.INFO, "schedule screen title : " +titleschedule);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrschedule);
		test.log(LogStatus.PASS, "Schedule button clicked");
		String scrshiftoffer = npo.VerifyShiftOffers(d);
		String titleshift = npo.shiftOfferScreenTitle(d);
		test.log(LogStatus.INFO, "Shift screen title : " +titleshift);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrshiftoffer);
		test.log(LogStatus.PASS, "Shift Offer button clicked");
		String scrmore = npo.verifyMore(d);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrmore);
		test.log(LogStatus.PASS, "More button clicked");
		String scrprofile = npo.verifyProfile(d);
		String titleprofile = npo.myProfileScreenTitle(d);
		npo.closeSetUp(d);
		test.log(LogStatus.INFO, "My Profile screen title : " +titleprofile);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrprofile);
		test.log(LogStatus.PASS, "profile link clicked");
		String scravaliabilty = npo.verifyAvailablity(d);
		String titlewheni = npo.whenIPreferScreenTitle(d);
		test.log(LogStatus.INFO, "When i Prefer screen title : " +titlewheni);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scravaliabilty);
		test.log(LogStatus.PASS, "Avaliabilty link clicked");
		test.log(LogStatus.PASS, "When i prefer to work link clicked");
		String scrwhenimbusy = npo.verifyWhenImBusy(d);
		String titlewhenbusy = npo.whenImBusyScreenTitle(d);
		npo.closeSetUp(d);
		test.log(LogStatus.INFO, "When im Busy screen title : " +titlewhenbusy);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrwhenimbusy);
		test.log(LogStatus.PASS, "When im busy link clicked");
		String scrwork = npo.verifyWorkPreferences(d);
		String titlehowi = npo.howIPreferScreenTitle(d);
		test.log(LogStatus.INFO, "How I Prfer to work screen title : " +titlehowi);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrwork);
		test.log(LogStatus.PASS, "work prefereneces link clicked");
		String scrwhat = npo.verifyWhatsImportant(d);
		String titlewhat = npo.whatsImportantScreenTitle(d);
		npo.closeSetUp(d);
		test.log(LogStatus.INFO, "What important screen title : " +titlewhat);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrwhat);
		test.log(LogStatus.PASS, "What's important to me link clicked");
		String scrsetting = npo.verifySettings(d);
		String titlesettings = npo.settingsScreenTitle(d);
		npo.closeSettings(d);
		test.log(LogStatus.INFO, "Home screen title : " +titlesettings);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrsetting);
		test.log(LogStatus.PASS, "Settings link clicked");
		String scrabout = npo.verifyAboutLegion(d);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrabout);
		test.log(LogStatus.PASS, "About legion link clicked");
		String scrprivacy = npo.verifyPrivacyPolicy(d);
		String titleprivacy = npo.privacyScreenTitle(d);
		npo.closeSetUp(d);
		test.log(LogStatus.INFO, "Privacy and Policy screen title : " +titleprivacy);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrprivacy);
		test.log(LogStatus.PASS, "Privacy policy link clicked");
		String scrterm = npo.verifyTermsOfUse(d);
		String titleterms = npo.termsScreenTitle(d);
		npo.closeSetUp(d);
		test.log(LogStatus.INFO, "Terms of Use title : " +titleterms);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrterm);
		test.log(LogStatus.PASS, "Terms of use link clicked");
		npo.verifyFeedback(d);
		String titlefeedback = npo.FeedbackScreenTitle(d);
		String scrfeedback = npo.feedbackNavigation(d);
		test.log(LogStatus.INFO, "Feedback screen title : " +titlefeedback);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scrfeedback);
		test.log(LogStatus.PASS, "send feedback link clicked");
		 npo.verifyOnBoarding(d);
		String titleonboarding = npo.onBoardingScreenTitle(d);
		String scronboarding =npo.onBoardingNavigations(d);
		test.log(LogStatus.INFO, "Home screen title : " +titleonboarding);
		test.log(LogStatus.INFO, "Screenshot Taken : "+scronboarding);
		test.log(LogStatus.PASS, "On-Boarding link clicked");
		test.log(LogStatus.INFO, "Logout link clicked");
		logger.info("navigations are clicked");
		test.log(LogStatus.PASS, "Navigation screens test passed");
	}

	// Method for failed reports
	@AfterMethod
	public void result(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(LogStatus.FAIL, "Test failed");
		}
		reports.endTest(test);
		reports.flush();
	}

	// Close the app, destroy the extent reports objects and stop the appium
	// server
	@AfterClass
	public void teardown() {
		d.closeApp();
		reports.endTest(test);
		reports.flush();
		service.stop();
		System.out.println("current status:" + service.isRunning());
	}
}
