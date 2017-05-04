package mobileutilities;

import java.awt.Dimension;
import java.io.File;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class BaseClass
{
	
	public static WebElement element;
	public static AppiumDriver d;
	public static String versionString;
	public static int version;
	public static String pth=System.getProperty("user.dir");
	public static String reportFilePath=pth+"/Reports/";
	public static String screenshotFilePath=pth+"/screenshots/";
	public static AppiumDriverLocalService service;
	public static AndroidDriver<MobileElement> driver;
	// Set the Desired Capabilities to launch the app in Andriod mobile
	public static void launchMobileApp(int version) throws Exception {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "Android");
		caps.setCapability("platformName", "Android");

		switch (version) {
		case 1:
			versionString = "4.4.2";
			break;
		case 2:
			versionString = "4.4.3";
			break;
		case 3:
			versionString = "4.4.4";
		case 4:
			versionString = "5.0";
			break;
		case 5:
			versionString = "5.0.1";
			break;
		case 6:
			versionString = "5.0.2";
		case 7:
			versionString = "5.1";
			break;
		case 8:
			versionString = "5.1.1";
			break;
		case 9:
			versionString = "6";
		case 10:
			versionString = "6.0.1";
			break;
		case 11:
			versionString = "7";
			break;
		case 12:
			versionString = "7.1";
			break;
		}
		caps.setCapability("platformVersion", versionString);
		caps.setCapability("autoAcceptAlerts", true);
		caps.setCapability("appPackage", "co.legion.client.staging");
		caps.setCapability("appActivity", "co.legion.client.activities.LegionSplashActivity");
		d = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);	
		}

	// Explicit wait method for element clickable
	public static WebElement waitForExpectedElement(AppiumDriver d, final By locator) {
	  WebDriverWait wait = new WebDriverWait(d, 100);
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	// Explicit wait method for presence of element
	public static WebElement waitForExpectedElement(AppiumDriver d, final By locator, int time) {
		WebDriverWait wait = new WebDriverWait(d, time);
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public static void waitForPageToLoad() {
		(new WebDriverWait(d, 60)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply1(AppiumDriver d) {
				return (((org.openqa.selenium.JavascriptExecutor) d).executeScript("return document.readyState")
						.equals("complete"));
			}

			public Boolean apply(AppiumDriver arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			public Boolean apply(WebDriver arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
// Method for Select the value from drop down list by name
	public static void ajaxDroDownSelect(AppiumDriver d, By locator, String name) {
		Select se = new Select((WebElement) locator);
		se.selectByVisibleText(name);
	}
	// Method for Select the value from drop down list by index
	public static void ajaxdropDownSelect(AppiumDriver d, By locator, int index) {
		Select se = new Select((WebElement) locator);
		se.selectByIndex(index);
	}
// Method to hide the key board
	public static void key(AppiumDriver d)
	{
		try
		{
			d.hideKeyboard();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
	// Method for Start the appium server and arguments should be appium installation path upto node.exe and appium.js
	public static void appiumServerStart(String appiumServerPath, String appiumJSPath){
		service=AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
				.usingDriverExecutable(new File(appiumServerPath))
				.withAppiumJS(new File(appiumJSPath)));
	}
	//Method for Retry and It excutes the failed test case based on our count
	public class Retry implements IRetryAnalyzer {
		 
	    private int count = 0;
	    private static final int maxTry = 3;
	 
	    public boolean retry(ITestResult iTestResult) {
	        if (!iTestResult.isSuccess()) {                      //Check if test not succeed
	            if (count < maxTry) {                            //Check if maxtry count is reached
	                count++;                                     //Increase the maxTry count by 1
	                iTestResult.setStatus(ITestResult.FAILURE);  //Mark test as failed
	                return true;                                 //Tells TestNG to re-run the test
	            } else {
	                iTestResult.setStatus(ITestResult.FAILURE);  //If maxCount reached,test marked as failed
	            }
	        } else {
	            iTestResult.setStatus(ITestResult.SUCCESS);      //If test passes, TestNG marks it as passed
	        }
	        return false;
	    }
	 
	}
	public static void popUp(AppiumDriver d)
	{
		try
		{
	
			if(d.findElementById("//co.legion.client.staging:id/msgTV").isDisplayed())
			{
				System.out.println(d.findElementByName("Your request has been timed out. Please try again later.").getText());
				d.findElementById("co.legion.client.staging:id/okTV").click();
			}
			else if(d.findElementById("//co.legion.client.staging:id/msgTV").isDisplayed())
			{
				Dimension dimensions = new Dimension();
				d.manage().window().getSize();
		       int screenWidth = (int) dimensions.getWidth();
		       int screenHeight = (int) dimensions.getHeight();
				d.tap(1, screenWidth, screenHeight, 1000);
			}
			else
			{
				System.out.println(d.findElementById("//co.legion.client.staging:id/msgTV").getText());
				d.findElementById("co.legion.client.staging:id/okTV").click();
			}
		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}	
}



