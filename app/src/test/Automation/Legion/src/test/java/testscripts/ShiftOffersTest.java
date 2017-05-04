package testscripts;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
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
import pageobjects.ShiftOffersPageObjects;

@Test
public class ShiftOffersTest extends mobileutilities.BaseClass {

	// create variable for extent reports
	ExtentReports reports;
	// create variable for extent test
	ExtentTest test;
	// create variable for Itest results
	ITestResult result;
	// creating object gor logger class
	Logger logger = Logger.getLogger(ShiftOffersTest.class);
	// create object for ScheduleOverviewPageObjects class

	ShiftOffersPageObjects so = new ShiftOffersPageObjects(d);
	// create object for LoginScreenPageObjects class
	LoginScreenPageObjects lspo = new LoginScreenPageObjects(d);
	ConfigFileUtility cfru = new ConfigFileUtility();
	// create variable version as integer
	int version = 10;

	// constructor
	ShiftOffersTest() throws Exception {
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
		reports = new ExtentReports(reportFilePath +Utilities.date()+"_"+ "ShiftOffers.html", true);
		launchMobileApp(version);
	}
	@BeforeMethod
	public void login()
	{
		String email;
		test = reports.startTest("Verify Shift offer Screen Functionality");
		logger.info("open screen");
		email = lspo.login(d, cfru.getProperty("email"));
		logger.info("Enter vaild email address");
		test.log(LogStatus.INFO, "Email Details filled with : " + email);
		logger.info("Enter vaild password");
		test.log(LogStatus.INFO, "password Details filled");
		logger.info("Login button clicked");
		test.log(LogStatus.INFO, "Login button clicked");
		test.log(LogStatus.INFO, "Shift Offers button clicked");
		test.log(LogStatus.PASS, "Verified shift offer Screen navigation");
		reports.endTest(test);
	}
	@Test
	public void ShiftOffers() {
		so.unclaimedOperation(d);
		test.log(LogStatus.INFO, "Clicked unclaimed offers");
		String ucocalloutcount=so.callOutsCount(d);
		test.log(LogStatus.INFO, "My Approved Offers : " +ucocalloutcount);
		String ucocalloutscreen=so.calloutScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +ucocalloutscreen);
		String ucoswapcount=so.swapCount(d);
		test.log(LogStatus.INFO, "My Approved offers : " +ucoswapcount);
		String ucoswapscreen=so.swapScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : "+ucoswapscreen);
		String ucoshiftcount=so.openShiftsCount(d);
		test.log(LogStatus.INFO, "My Approved offers : " +ucoshiftcount);
		String ucoshiftscreen=so.openShiftScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +ucoshiftscreen);
		String ucoswap=so.unClaimedSwap(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +ucoswap);
		test.log(LogStatus.INFO, "Clicked swap offers");
		test.log(LogStatus.INFO, "Clicked pending offers");
		test.log(LogStatus.INFO, "Clicked rejected offers");
		so.unClaimedOpenShifts(d);
		test.log(LogStatus.INFO, "Clicked openshift offers");
		so.unclaimedoffers(d);
		test.log(LogStatus.PASS, "Verified UnClaimed Offers Functionality");
		String noflistscreen=so.newOffersfunctionality(d);
		test.log(LogStatus.INFO, "Screenshot taken : "+noflistscreen);
		String nofcalloutcount=so.callOutsCount(d);
		test.log(LogStatus.INFO, "My Approved Offers : " +nofcalloutcount);
		String nofcalloutscreen=so.calloutScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +nofcalloutscreen);
		String nofswapcount=so.swapCount(d);
		test.log(LogStatus.INFO, "My Approved offers : " +nofswapcount);
		String nofswapscreen=so.swapScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : "+nofswapscreen);
		String nofshiftcount=so.openShiftsCount(d);
		test.log(LogStatus.INFO, "My Approved offers : " +nofshiftcount);
		String nofshiftscreen=so.openShiftScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +nofshiftscreen);
		test.log(LogStatus.PASS, "Verfied New Offers in swap Functionality");
		test.log(LogStatus.PASS, "Verified New Offers Preview and Claimed Functionality");
		so.myApprovedOffers(d);
		String maocalloutcount=so.callOutsCount(d);
		test.log(LogStatus.INFO, "My Approved Offers : " +maocalloutcount);
		String maocalloutscreen=so.calloutScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +maocalloutscreen);
		String maoswapcount=so.swapCount(d);
		test.log(LogStatus.INFO, "My Approved offers : " +maoswapcount);
		String maoswapscreen=so.swapScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : "+maoswapscreen);
		String maoshiftcount=so.openShiftsCount(d);
		test.log(LogStatus.INFO, "My Approved offers : " +maoshiftcount);
		String maoshiftscreen=so.openShiftScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +maoshiftscreen);
		so.myApprovedswap(d);
		so.myApprovedOffersNavigations(d);
		test.log(LogStatus.PASS, "Verified My Approved Offers Functionality");
		so.myPendingOffers(d);
		String mpocalloutcount=so.callOutsCount(d);
		test.log(LogStatus.INFO, "My Pending Offers : " +mpocalloutcount);
		String mpocalloutscreen=so.calloutScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +mpocalloutscreen);
		String mposwapcount=so.swapCount(d);
		test.log(LogStatus.INFO, "My Pending offers : " +mposwapcount);
		String mposwapscreen=so.swapScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : "+mposwapscreen);
		String mposhiftcount=so.openShiftsCount(d);
		test.log(LogStatus.INFO, "My Pending offers : " +mposhiftcount);
		String mposhiftscreen=so.openShiftScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +mposhiftscreen);
		so.myPendingswap(d);
		so.myPendingOffersNavigations(d);
		test.log(LogStatus.PASS, "Verified My Pending Offers Functionality");
		so.myRejectedOffers(d);
		String mrocalloutcount=so.callOutsCount(d);
		test.log(LogStatus.INFO, "My Rejected Offers : " +mrocalloutcount);
		String mrocalloutscreen=so.calloutScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +mrocalloutscreen);
		String mroswapcount=so.swapCount(d);
		test.log(LogStatus.INFO, "My Rejected offers : " +mroswapcount);
		String mroswapscreen=so.swapScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : "+mroswapscreen);
		String mroshiftcount=so.openShiftsCount(d);
		test.log(LogStatus.INFO, "My Rejected offers : " +mroshiftcount);
		String mroshiftscreen=so.openShiftScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +mroshiftscreen);
		so.myRejectedswap(d);
		so.myRejectedOffersNavigation(d);
		test.log(LogStatus.PASS, "Verified my Rejected Offers functionality");
		so.myBookmarks(d);
		String mbmcalloutcount=so.callOutsCount(d);
		test.log(LogStatus.INFO, "My BookMarks : " +mbmcalloutcount);
		String mbmcalloutscreen=so.calloutScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +mbmcalloutscreen);
		String mbmswapcount=so.swapCount(d);
		test.log(LogStatus.INFO, "My BookMarks : " +mbmswapcount);
		String mbmswapscreen=so.swapScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : "+mbmswapscreen);
		String mbmshiftcount=so.openShiftsCount(d);
		test.log(LogStatus.INFO, "My BookMarks : " +mbmshiftcount);
		String mbmshiftscreen=so.openShiftScreen(d);
		test.log(LogStatus.INFO, "Screenshot taken : " +mbmshiftscreen);
		so.myBookmarksswap(d);
		so.myBookMarksNavigation(d);
		test.log(LogStatus.PASS, "Verified My BookMarks functionality");
		lspo.logout(d);
		logger.info("Clicked ShiftOffers ");
		test.log(LogStatus.INFO, "Clicked ShiftOffers");
		test.log(LogStatus.PASS, "ShiftOffers test passed");
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
