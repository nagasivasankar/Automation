package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import mobileutilities.BaseClass;
import mobileutilities.Utilities;

public class HowIPreferToWorkPageObjects extends BaseClass{

	// create driver
	public AppiumDriver d;
	// create variable of gettingtitle as static final string type
	public static final String GETTINGTITLE = "co.legion.client.staging:id/title";
	// create variable of gettingtext as static final string type
	public static final String GETTINGTEXT = "co.legion.client.staging:id/buttonHow_I_Prefer";
	// create variable of shift_length as static final string type
	public static final String SHIF_LENGTH = "co.legion.client.staging:id/shiftLengthSeekbar";
	// create variable of btntoggle as static final string type
	public static final String BTNTOGGLE = "co.legion.client.staging:id/locationSwitch";
	// create variable of gettingtextbottom as static final string type
	public static final String GETTINGTEXTBOTTOM = "co.legion.client.staging:id/toggleText";
	// create variable of more as static final string type
	public static final String MORE = "//android.widget.TextView[@text='More']";
	// create variable of lnkworkprefernces as static final string type
	public static final String LNKWORKPREFERENCE = "co.legion.client.staging:id/work_preferences";
	// create variable of slider as static final string type
	public static final String SLIDER = "co.legion.client:id/shiftLengthSeekbar";
	// create variable of navigationok as static final string type
	public static final String NAVIAGATIONOK = "co.legion.client.staging:id/imageviewOk";
	// create variable of navigationcancel as static final string type
	public static final String NAVIAGATIONCANCEL = "co.legion.client.staging:id/closeSetup";
	// create variable of btndiscard as static final string type
	public static final String BTNDISCARD = "co.legion.client.staging:id/discardTV";
	// create variable of BTNOK as static final string type
	public static final String BTNOK = "co.legion.client.staging:id/okTV";
	// create variable of NAIGATIONCANCEL as static final string type
	public static final String NAVIGATIONCANCEL = "co.legion.client.staging:id/closeSetup";

	// constructor
	public HowIPreferToWorkPageObjects(AppiumDriver driver) {
		this.d = driver;
	}

	// method for how i prefer to work screen in the app
	public void howIPrefertoWork(AppiumDriver d) {
		WebDriverWait wait = new WebDriverWait(d, 30);
		try {
			d.findElementById(NAVIGATIONCANCEL).click();
		} catch (Exception e) {
			e.getMessage();
			popUp(d);
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MORE)));
		d.findElementByXPath(MORE).click();
		d.findElementById(LNKWORKPREFERENCE).click();
	}
	
	public String verifyPageTitle(AppiumDriver d)
	{
		String pagetitle = null;
		try 
		{
			pagetitle=d.findElementById(GETTINGTITLE).getText();
			System.out.println("Text of header : " +pagetitle);			
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return pagetitle;
		
	}
	public String verifyText(AppiumDriver d)
	{
		String text=null;
		try
		{
			text=d.findElementById(GETTINGTEXT).getText();
			System.out.println("Text : " + text);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return text;
	}
	public String verifyFooterContent(AppiumDriver d)
	{
		String bottomtext=null;
		try
		{
			bottomtext = d.findElementById(GETTINGTEXTBOTTOM).getText();
			System.out.println("Text near toggle : " + bottomtext);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return bottomtext;
	}
	public String[] toggle(AppiumDriver d)
	{
		String screensht[]={"0","0"};
		try
		{
			WebDriverWait wait = new WebDriverWait(d, 30);
			screensht[0]=Utilities.captureScreenshot(d, "toggle before");
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNTOGGLE)));
			d.findElementById(BTNTOGGLE).click();
			screensht[1]=Utilities.captureScreenshot(d, "toggle after");
			d.findElementById(NAVIAGATIONCANCEL).click();
			d.findElementById(BTNDISCARD).click();
			d.findElementById(NAVIAGATIONOK).click();
			d.findElementById(BTNOK).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(NAVIGATIONCANCEL)));
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
