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
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import junit.framework.Assert;
import mobileutilities.ConfigFileUtility;
import mobileutilities.Utilities;
import mobileutilities.BaseClass.Retry;
import pageobjects.LoginScreenPageObjects;
import pageobjects.MyProfilePageObjects;

public class MyProfileTest extends mobileutilities.BaseClass {
	// create variable for extent reports
	ExtentReports reports;
	// create variable for extent test
	ExtentTest test;
	// create variable for Itest results
	ITestResult result;
	// creating object gor logger class
	Logger logger = Logger.getLogger(LoginScreenTest.class);
	// create object for MyProfilePageObjects class
	MyProfilePageObjects mpg = new MyProfilePageObjects(d);
	// create object for LoginScreenPageObjects class
	LoginScreenPageObjects lspo = new LoginScreenPageObjects(d);
	// create object for configfileutility class
	ConfigFileUtility cfru = new ConfigFileUtility();
	// create variable version as integer
	int version = 10;

	// constructor
	MyProfileTest() throws Exception {
		cfru.loadPropertyFile();
		PropertyConfigurator.configure("log4j.properties");
	}

	@BeforeSuite
	public void startServer() {
		appiumServerStart(cfru.getProperty("appiumServerPath"), cfru.getProperty("appiumJsPath"));
	}

	@BeforeClass
	public void launchApplication() throws Exception {
		service.start();

		reports = new ExtentReports(reportFilePath + Utilities.date()+"_"+"Myprofile.html", true);
		//test = reports.startTest("Verify the Legion App Launching");
		launchMobileApp(version);
	/*	test.log(LogStatus.INFO, "App launched");
		test.log(LogStatus.INFO, "Application is up and running");
		test.log(LogStatus.PASS, "Verified login screen launched");
		reports.endTest(test);*/

	}
/*	@BeforeMethod
	public void login()
	{
		String email;
		test = reports.startTest("Verify My Profile Screen navigation");
		logger.info("open screen");
		email = lspo.login(d, cfru.getProperty("email"));
		logger.info("Enter vaild email address");
		test.log(LogStatus.INFO, "Email Details filled with : " + email);
		logger.info("Enter vaild password");
		test.log(LogStatus.INFO, "password Details filled");
		logger.info("Login button clicked");
		test.log(LogStatus.PASS, "Verified home screen navigation");
		reports.endTest(test);
	}*/
	@Test(priority = 1,retryAnalyzer = Retry.class)
	public void loginScreen() {
		String email;
		test = reports.startTest("Verify My Profile Screen navigation");
		WebDriverWait wait = new WebDriverWait(d, 30);
		test.log(LogStatus.INFO, "Fill the login credentials ");
		logger.info("Enter vaild email address");
		email = lspo.login(d, cfru.getProperty("email"));
		test.log(LogStatus.INFO, "Email Details filled with :  " + email);
		logger.info("Enter vaild password");
		test.log(LogStatus.INFO, "password Details filled");
		logger.info("Login button clicked");
		test.log(LogStatus.INFO, "Login button clicked");
		test.log(LogStatus.INFO, "More  button clicked");
		test.log(LogStatus.INFO, "My Profile button clicked");
		test.log(LogStatus.PASS, "Verified home screen navigation");
	}
	

	@Test(dataProvider = "myprofile", priority = 2,retryAnalyzer = Retry.class)
	public void myProfile(String firstname, String lastname, String nickname, String mobileno, String emailid,
			String address, String city, String pincode, String cond) throws Exception {

		if (cond == "valid")
			test = reports.startTest("Verify the Myprofile Screen with valid data");
		else
			test = reports.startTest("Verify the Myprofile Screen with invalid data");
		;
		logger.info("home screen opened");
		mpg.myProfile(d, firstname, lastname, nickname, mobileno, emailid, address, city, pincode, cond);
		logger.info("Entered the first name");
		test.log(LogStatus.INFO, " Firstname details filled : " + firstname);
		logger.info("Entered the last name");
		test.log(LogStatus.INFO, "Lastname details filled : " + lastname);
		logger.info("Entered the nick name");
		test.log(LogStatus.INFO, "Nickname details filled: " + nickname);
		logger.info("Entered the mobile number");
		test.log(LogStatus.INFO, "Mobile number details filled: " + mobileno);
		logger.info("Entered the email id");
		test.log(LogStatus.INFO, "Emailid details filled : " + emailid);
		logger.info("Entered the address");
		test.log(LogStatus.INFO, "Address details filled: " + address);
		logger.info("Entered the city");
		test.log(LogStatus.INFO, "City details filled: " + city);
		logger.info("Entered the state");
		test.log(LogStatus.INFO, "State details filled");
		logger.info("Entered the pincode");
		test.log(LogStatus.INFO, "Pincode details filled : " + pincode);
		logger.info("Clicked next");
		try {
			String[] str = mpg.myProfileError(d);
			for (String error : str)
				test.log(LogStatus.INFO, "Application displaying error message as : " + error);
			test.log(LogStatus.PASS, "My Profile Test Passed");
			String screen=Utilities.captureScreenshot(d, "Invalid data My Profile");
			test.log(LogStatus.INFO, "Screenshot taken : "+screen);
			
		} catch (Exception e) {
			e.getMessage();
			if (cond == "valid") {

				test.log(LogStatus.PASS, "My Profile Test Passed");
				String screen=Utilities.captureScreenshot(d, "valid data My Profile");
				test.log(LogStatus.INFO, "Screenshot taken : "+screen);
			} else {
				test.log(LogStatus.FAIL, "Failed error message is not displaying");
			}

		}
		mpg.save(d);
		reports.endTest(test);
	}

	@Test(priority = 3)
	public void logout() {
		lspo.logout(d);
	}

	@DataProvider(name = "myprofile")
	public Object[][] getData() {
		Object[][] data = new Object[3][9];
		// 1st row
		data[0][0] = "";
		data[0][1] = "";
		data[0][2] = "";
		data[0][3] = "";
		data[0][4] = "";
		data[0][5] = "";
		data[0][6] = "";
		data[0][7] = "";
		data[0][8] = "invalid";
		// 2nd row
		data[1][0] = "Gayi567 ";
		data[1][1] = "Chan567";
		data[1][2] = "hvgi&^*";
		data[1][3] = "85hgujkji";
		data[1][4] = "g@gmail ";
		data[1][5] = "hgy789/2 ";
		data[1][6] = "Hyd246";
		data[1][7] = "54gh5";
		data[1][8] = "invalid";
		// 9th row
		data[2][0] = "lina";
		data[2][1] = "deleon";
		data[2][2] = "lina246";
		data[2][3] = "408-000-0017";
		data[2][4] = "lina.deleon@legion.co";
		data[2][5] = "13-8-42";
		data[2][6] = "Colombia";
		data[2][7] = "85868";
		data[2][8] = "valid";
		return data;

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
		// closing app
		reports.endTest(test);
		reports.flush();
		d.closeApp();
		service.stop();
		System.out.println("current status:" + service.isRunning());
	}
}
