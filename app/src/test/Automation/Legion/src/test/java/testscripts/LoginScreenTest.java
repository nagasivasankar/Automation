package testscripts;



import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


import mobileutilities.BaseClass;
import mobileutilities.ConfigFileUtility;
import mobileutilities.Utilities;
import pageobjects.LoginScreenPageObjects;

public class LoginScreenTest extends BaseClass { // create variable for extent
													// reports
	ExtentReports reports;
	// create variable for extent test
	ExtentTest test;
	// create variable for Itest results
	ITestResult result;
	// creating object logger logger class
	Logger logger = Logger.getLogger(LoginScreenTest.class);
	// create object for LoginScreenPageObjects class
	LoginScreenPageObjects lg = new LoginScreenPageObjects(d);
	ConfigFileUtility cfru = new ConfigFileUtility();
	// create variable version as integer
	int version = 10;

	// constructor
	LoginScreenTest() throws Exception {
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
		reports = new ExtentReports(reportFilePath + Utilities.date()+"_"+"Loginscreen.html", true);
	//	test = reports.startTest("Verify the Legion App Launching");
		launchMobileApp(version);
	/*	test.log(LogStatus.INFO, "App launched");
		test.log(LogStatus.INFO, "Application is up and running");
		test.log(LogStatus.PASS, "Verified login screen launched");
		reports.endTest(test);*/
	}

	@Test(priority = 1, retryAnalyzer = Retry.class)
	public void ui(){
		test = reports.startTest("Login UI");
		logger.info("Open Screen");
		String[] screensht=lg.gettingText(d);
		String text=lg.verifyFooterContent(d);
		test.log(LogStatus.PASS, "Verified create an account navigation");
		for(String screen:screensht)
			test.log(LogStatus.INFO, "Screenshot details: "+screen);
		test.log(LogStatus.PASS, "Verified footer content : "+text);
		reports.endTest(test);
	}

	@Test(dataProvider = "empLogin", priority = 2, retryAnalyzer = Retry.class)
	public void login(String username, String password, String cond) {

		if (cond == "valid") {
			test = reports.startTest("Verify the Login Screen with valid credentials");
		} else {
			test = reports.startTest("Verify the Login Screen with invalid credentials");
		}
		logger.info("open screen");
		lg.loginFun(d, username, password);
		test.log(LogStatus.INFO, "Login with data");
		logger.info("Enter the email address");
		test.log(LogStatus.INFO, "Email Details filled : " + username);
		logger.info("Enter the password");
		test.log(LogStatus.INFO, "password Details filled : " + password);
		logger.info("Login button clicked");
		test.log(LogStatus.INFO, "Login button clicked");
		if (cond == "invalid") {
			try {
				String str1 = lg.errorTextEmail(d);
				String str2 = lg.errorTextPassword(d);
				if(str1.equalsIgnoreCase(null))
					test.log(LogStatus.INFO, "Application displaying Error Message as : " + str2);
				else
				{
					test.log(LogStatus.INFO, "Application displaying Error Message as : " + str1);
					test.log(LogStatus.INFO, "Application displaying Error Message as : " + str2);
				}
				test.log(LogStatus.PASS, "Loginscreen Test Passed");
				String screen=Utilities.captureScreenshot(d, "login passed");
				test.log(LogStatus.INFO, "Screenshot taken : "+screen);
			} catch (Exception e) {
				logger.info(e.getMessage());
				String str2 = lg.errorTextPassword(d);
					test.log(LogStatus.INFO, "Application displaying Error Message as : " + str2);
					test.log(LogStatus.PASS, "Loginscreen Test Passed");
			}
		} 
		else 
		{
			String screen=Utilities.captureScreenshot(d, "login passed");
			test.log(LogStatus.INFO, "Screenshot taken : "+screen);
			lg.letsGetStarted(d);
			lg.waitMore(d);
			lg.logout(d);
			test.log(LogStatus.PASS, "Loginscreen Test Passed");

		}
		reports.endTest(test);
	}

	@DataProvider(name = "empLogin")
	public Object[][] getData() {
		Object[][] data = new Object[3][3];
		// 1st row
		data[0][0] = "";
		data[0][1] = "";
		data[0][2] = "invalid";
		// 2nd row
		data[1][0] = "lina.deleon@legion.co";
		data[1][1] = "hfkimr";
		data[1][2] = "invalid";
		// 3th row
		data[2][0] = "lina.deleon@legion.co";
		data[2][1] = "legionco";
		data[2][2] = "valid";
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
		d.closeApp();
		reports.endTest(test);
		reports.flush();
		service.stop();
		System.out.println("current status:" + service.isRunning());
	}
}
