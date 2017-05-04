package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import mobileutilities.Utilities;

public class WhatsImportantToMePageObjects extends mobileutilities.BaseClass
{
	
	//create driver 
	public AppiumDriver d;
	//create variable of GETTINGSCREENTITLE as static final string type
	public static final String GETTINGSCREENTITLE= "co.legion.client.staging:id/buttonMyPreferences";
	//create variable of CHKBOXWORKING as static final string type
	public static final String CHKBOXWORKING= "co.legion.client.staging:id/workingCheck";
	//create variable of CHKBOXCONSISTENCY as static final string type
	public static final String CHKBOXCONSISTENT= "co.legion.client.staging:id/consistentCheck";
	//create variable of CHKBOXVARIETY as static final string type
	public static final String CHKBOXVARIETY= "co.legion.client.staging:id/verityCheck";
	//create variable of CHKBOXFLEXIBILITY as static final string type
	public static final String CHKBOXFLEXIBILITY= "co.legion.client.staging:id/flexCheck";
	//create variable of GETTINGTEXT1 as static final string type
	public static final String GETTINGTEXT1 = "//android.widget.TextView[@index='0']";
	//create variable of GETTINGTEXT as static final string type
	public static final String GETTINGTEXT = "//android.widget.TextView[@index='1']";
	//create variable of BTNMYPREFERENCES as static final String type
	public static final String BTNMYPREFERENCES="co.legion.client.staging:id/buttonMyPreferences";
	//create variable of BTNMORE as static final string type
	public static final String BTNMORE="//android.widget.TextView[@text='More']";
	//create variable of LNKWORK-PREFERENCES as static final string type
	public static final String LNKWORK_PREFERENCES="//android.widget.TextView[@text='Work Preferences']";
	//create variable of NAVIGATIONOK as static final string type
	public static final String NAVIAGATIONOK="co.legion.client.staging:id/imageviewOk";
	//create variable of NAVIGATIONCANCEL as static final string type
	public static final String NAVIAGATIONCANCEL="co.legion.client.staging:id/closeSetup";
	//create variable of BTNDISCARD as static final string type
	public static final String BTNDISCARD="co.legion.client.staging:id/discardTV";
	//create variable of BTNOK as static final string type
	public static final String BTNOK="co.legion.client.staging:id/okTV";
	
	//constructor
	public WhatsImportantToMePageObjects(AppiumDriver driver)
	{
		this.d=driver;
	}
	// Method for clicking the link
	public void click_Link_Button(By locator)
	{
		d.findElement(locator).click();
	}

	//Method for My Priorities screen
	public void myPerferencesSave(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d,10);
		try
		{
			d.findElementById(NAVIAGATIONCANCEL).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNMORE)));
		d.findElementByXPath(BTNMORE).click();
		d.findElementByXPath(LNKWORK_PREFERENCES).click();
		d.findElementById(BTNMYPREFERENCES).click();
	}
	public String verifyPageTitle(AppiumDriver d)
	{
		String titletext=null;
		try
		{
			titletext=d.findElementByXPath(GETTINGTEXT1).getText();
			System.out.println("Text of header: "+ titletext);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return titletext;
	}
	public String verifytext(AppiumDriver d)
	{
		String titletext=null;
		try
		{
			titletext=d.findElementById(GETTINGSCREENTITLE).getText();
			System.out.println("Text of header: "+titletext);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return titletext;
	}
	public String checkboxtext(AppiumDriver d)
	{
		String chxboxtext=null;
		try
		{
			chxboxtext=d.findElementByXPath(GETTINGTEXT).getText();
			System.out.println("Text of header: "+chxboxtext);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return chxboxtext;
	}
	public String[] checkboxClick(AppiumDriver d)
	{
		String screensht[]={"0","0"};
		try
		{
			screensht[0]=Utilities.captureScreenshot(d, "Before check box Clicking");
			d.findElementById(CHKBOXWORKING).click();
			d.findElementById(CHKBOXCONSISTENT).click();
			d.findElementById(CHKBOXVARIETY).click();
			d.findElementById(CHKBOXFLEXIBILITY).click();
			screensht[1] = Utilities.captureScreenshot(d, "after check box Clicking");
			d.findElementById(NAVIAGATIONCANCEL).click();	
			d.findElementById(BTNDISCARD).click();
			d.findElementById(NAVIAGATIONOK).click();
			d.findElementById(BTNOK).click();
			d.findElementById(NAVIAGATIONCANCEL).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}

}
