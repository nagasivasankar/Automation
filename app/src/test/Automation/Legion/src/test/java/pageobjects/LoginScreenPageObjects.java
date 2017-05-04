package pageobjects;

import java.awt.Dimension;
import java.awt.Window;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import com.sun.jna.platform.unix.X11.Display;

import io.appium.java_client.AppiumDriver;
import mobileutilities.BaseClass;
import mobileutilities.Utilities;

public class LoginScreenPageObjects extends BaseClass
{
	//create driver 
	public AppiumDriver d = null;
	//create variable of txtboxemail as static final string type
	public static final String TXTBOXEMAIL = "co.legion.client.staging:id/usernameET";
	//create variable of txtboxpasword as static final string type
	public static final String TXTBOXPASWORD = "co.legion.client.staging:id/passwordEditText";
	//create variable of btnlogin as static final string type
	public static final String BTNLOGIN = "co.legion.client.staging:id/login";
	//create variable of lnkforgotpassword as static final string type
	public static final String LNKFORGOTPASSWORD ="co.legion.client.staging:id/forgotPassword";
	//create variable of lnkcreateaccount as static final string type
	public static final String LNKCREATEACCOUNT="co.legion.client.staging:id/createAccountTv";	
	//create variable of gettingtxtemailerror as static final string type
	public static final String GETTINGTXTEMAILERROR="co.legion.client.staging:id/usernameErrorText";	
	//create variable of gettingtxtpasserror as static final string type
	public static final String GETTINGTXTPASSERROR="co.legion.client.staging:id/passwordErrorText";	
	//create variable of btnmore as static final string type
	public static final String BTNMORE="//android.widget.TextView[@text='More']";
	//create variable of btnlogin1  as static final string type
	public static final String BTNLOGIN1= "co.legion.client.staging:id/loginBTN";
	//create variable of LNKLOGOUT as static final string type
	public static final String LNKLOGOUT = "co.legion.client.staging:id/logout";
	//create variable of GETTINGTXTTERM as static final string type
	public static final String GETTINGTXTTERM = "co.legion.client.staging:id/termofuse";
	//create variable of BTNLOGOUT as static final string type
	public static final String BTNLOGOUT = "co.legion.client.staging:id/saveTv";
	//create variable of BTNLETSSTARTED as static final string type
	public static final String BTNLETSSTARTED = "co.legion.client.staging:id/letStartedButton";
	//create variable of BTNCANCEL as static final string type
	public static final String BTNCANCEL = "co.legion.client.staging:id/closeSetup";
	//create variable of BTNPASSSATUTS as static final string type
	public static final String BTNPASSSTATUS = "co.legion.client.staging:id/passwordStatusImage";
	//create variable of LNKEMAILMORE as static final string type
	public static final String LNKEMAILMORE= "//android.widget.ImageView[@index='2']";
	//create variable of LNKEMAILDISCARD as static final string type
	public static final String LNKEMAILDISCARD = "//android.widget.TextView[@text='Discard']";
	//create variable of NAVIGATIONBACK as static final string type
	public static final String NAVIGATIONBACK = "co.legion.client.staging:id/toolbarBack";
	//create variable of BTNCREATEANACCOUNT as static final string type
	public static final String BTNCREATEANACCOUNT = "co.legion.client.staging:id/createAccountBTN";
	
	//Constructor
	public LoginScreenPageObjects(AppiumDriver driver)
	{
		this.d=driver;
	}

	//Method for Getting the text from Login screen 
	public String[] gettingText(RemoteWebDriver d) 
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNLOGIN1)));
		d.findElementById(BTNCREATEANACCOUNT).click();
		String screensht1= Utilities.captureScreenshot(d, "Create an account");
		d.findElementById(NAVIGATIONBACK).click();
		d.findElementById(BTNLOGIN1).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id(LNKFORGOTPASSWORD)));
		d.findElementById(LNKFORGOTPASSWORD).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(LNKEMAILMORE)));
		d.findElementByXPath(LNKEMAILMORE).click();
		String screensht= Utilities.captureScreenshot(d,"Forgot password");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(LNKEMAILDISCARD)));
		d.findElementByXPath(LNKEMAILDISCARD).click();
		return new String[] {screensht1,screensht};
	}
	public String verifyFooterContent(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(LNKFORGOTPASSWORD)));
		String text=d.findElementById(GETTINGTXTTERM).getText();
		System.out.println(text);
		return text;
	}
	//Method for login functionality with valid and invalid credentials
	public void loginFun(AppiumDriver d,String username, String password) 
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(TXTBOXEMAIL)));
		d.findElementById(TXTBOXEMAIL).clear();
		d.findElementById(TXTBOXEMAIL).sendKeys(username);
		key(d);
		WebElement element=d.findElementById(TXTBOXPASWORD);
		element.clear();
		try
		{
			d.findElementById(BTNPASSSTATUS).click();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		d.findElementById(TXTBOXPASWORD).sendKeys(password);
		key(d);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNLOGIN)));
		d.findElementById(BTNLOGIN).click();

	}
	
	//Method foe getting error messages text while giving invalid credentials
	public String errorTextEmail(AppiumDriver d)
	{
		String str=null;
		str = d.findElementById(GETTINGTXTEMAILERROR).getText();
		System.out.println("string is: "+str);
		return str;
	}
	
	//Method for getting error message text 
	public String errorTextPassword(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTPASSERROR)));
		String str1=null;
		str1= d.findElementById(GETTINGTXTPASSERROR).getText();
		System.out.println("string is: "+str1);
		return str1;
	}
	
	//Method for waiting until more button
	public void waitMore(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(LoginScreenPageObjects.BTNMORE)));
	}
	//Method for login to app with valid details
	public String login(AppiumDriver d,String email)
	{
		WebDriverWait wait=new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNLOGIN1)));
			d.findElementById(BTNLOGIN1).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		d.findElementById(TXTBOXEMAIL).clear();
		d.findElementById(TXTBOXEMAIL).sendKeys(email);
		key(d);
		WebElement element=d.findElementById(TXTBOXPASWORD);
		element.clear();
		try
		{
			d.findElementById(BTNPASSSTATUS).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		d.findElementById(TXTBOXPASWORD).sendKeys("legionco");
		key(d);
		d.findElementById(BTNLOGIN).click();
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNLETSSTARTED)));
			d.findElementById(BTNLETSSTARTED).click();
		
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}

		return email;
	}
	
	//Method for logout
	public void logout(AppiumDriver d)
	{
		try
		{
			WebDriverWait wait=new WebDriverWait(d,60);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNMORE)));
			d.findElementByXPath(BTNMORE).click();
			d.findElementById(LNKLOGOUT).click();
			d.findElementById(BTNLOGOUT).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	
	//Method for Letsgetstarted
	public void letsGetStarted(AppiumDriver d)
	{
		try
		{
			WebDriverWait wait=new WebDriverWait(d,60);
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNLETSSTARTED)));
			d.findElementById(BTNLETSSTARTED).click();
			d.findElementById(BTNCANCEL).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
}
