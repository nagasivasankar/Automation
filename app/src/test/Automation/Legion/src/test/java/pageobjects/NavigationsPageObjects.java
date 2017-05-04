package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import mobileutilities.BaseClass;
import mobileutilities.Utilities;

public class NavigationsPageObjects extends BaseClass {

	// Create driver
	public AppiumDriver d;
	// create variable of BTNHOME as static final string type
	public static final String BTNHOME = "//android.widget.TextView[@text='Home']";
	// create variable of BTNSCHEDULE as static final string type
	public static final String BTNSCHEDULE = "//android.widget.TextView[@text='Schedule']";
	// create variable of BTNSHIFTOFFER as static final string type
	public static final String BTNSHIFTOFFER = "//android.widget.TextView[@text='Shift Offers']";
	// create variable of BTNMORE as static final string type
	public static final String BTNMORE = "//android.widget.TextView[@text='More']";
	// create variable of LNKPROFILE as static final string type
	public static final String LNKPROFILE = "//android.widget.TextView[@text='Profile']";
	// create variable of LNKAVAILABLITY as static final string type
	public static final String LNKAVAILABLITY = "//android.widget.TextView[@text='Availability']";
	// create variable of LNKWORKPREFERENCES as static final string type
	public static final String LNKWORKPREFERENCES = "//android.widget.TextView[@text='Work Preferences']";
	// create variable of LNKABOUTLEGION as static final string type
	public static final String LNKABOUTLEGION = "co.legion.client.staging:id/arrow";
	// create variable of LNKLOGOUT as static final string type
	public static final String LNKLOGOUT = "//android.widget.TextView[@text='Logout']";
	// create variable of BTNWHENIMBUSY as static final string type
	public static final String BTNWHENIMBUSY = "co.legion.client.staging:id/whenIamBusy";
	// create variable of BTNMYPRIORITIES as static final string type
	public static final String BTNHOWIPREFER = "co.legion.client.staging:id/buttonHow_I_Prefer";
	// create variable of BTNMYPRIORITIES as static final string type
	public static final String BTNMYPRIORITIES = "co.legion.client.staging:id/buttonMyPreferences";
	// create variable of TILESTHIS_WEEK as static final string type
	public static final String TILESTHIS_WEEK = "//android.widget.TextView[@text='  THIS WEEK']";
	// create variable of TXTBOXFIRSTNAME as static final string type
	public static final String TXTBOXFIRSTNAME = "co.legion.client.staging:id/firstName";
	// create variable of BTNCANCEL as static final string type
	public static final String BTNCANCEL = "co.legion.client.staging:id/closeSetup";
	// create variable of NAVIGATIONNEXT as static final string type
	public static final String NAVIGATIONNEXT = "co.legion.client.staging:id/btn_next";
	// create variable of NAVIGATIONPREV as static final string type
	public static final String NAVIGATIONPREV = "co.legion.client.staging:id/btn_prev";
	// create variable of GETTINGTXTTITLE as static final string type
	public static final String GETTINGTXTTITLE = "co.legion.client.staging:id/title";
	// create variable of LNKSETTINGS as static final string type
	public static final String LNKSETTINGS = "co.legion.client.staging:id/notifi_settings";
	// create variable of NAVIGATIONTOOLBARBACK as static final string type
	public static final String NAVIGATIONTOOLBARBACK = "co.legion.client.staging:id/toolbarBack";
	// create variable of LNKPRIVACYPOLICY as static final string type
	public static final String LNKPRIVACYPOLICY = "co.legion.client.staging:id/privacy_policy";
	// create variable of LNKTERMSOFUSE as static final string type
	public static final String LNKTERMSOFUSE = "co.legion.client.staging:id/terms_of_use";
	// create variable of LNKSENDFEEDBACK as static final string type
	public static final String LNKSENDFEEDBACK = "co.legion.client.staging:id/sendFeedback";
	// create variable of LNKONBOARDING as static final string type
	public static final String LNKONBOARDING = "//android.widget.TextView[@text='On-Boarding']";
	// create variable of NAVIGATIONTOOL_BAR as static final string type
	public static final String NAVIAGTIONTOOL_BAR = "co.legion.client.staging:id/toolbarBack";
	// create variable of LNKEMAILMORE as static final string type
	public static final String LNKEMAILMORE = "//android.widget.ImageView[@index='2']";
	// create variable of LNKEMAILDISCARD as static final string type
	public static final String LNKEMAILDISCARD = "//android.widget.TextView[@text='Discard']";
	// create variable of BTNLETSSTARTED as static final string type
	public static final String BTNLETSSTARTED = "co.legion.client.staging:id/letStartedButton";
	// create variable of BTNLOGOUT as static final string type
	public static final String BTNLOGOUT = "co.legion.client.staging:id/saveTv";
	// create variable of BTNDISCARD as static final string type
	public static final String BTNDISCARD = "co.legion.client.staging:id/cancelTV";
	// create variable of BTNOKBUTTON as static final string type
	public static final String BTNOKBUTTON = "co.legion.client.staging:id/okButton";
	// create variable of BTNGOTIT as static final string type
	public static final String BTNGOTIT = "co.legion.client.staging:id/gotItTv";
	// create variable of GETTINGTXTHOME as static final string type
	public static final String GETTINGTXTHOME = "co.legion.client.staging:id/dayWishesTV";
	// create variable of GETTINGTXTSCHEDULE as static final string type
	public static final String GETTINGTXTSCHEDULE = "co.legion.client.staging:id/tv_title";
	// create variable of GETTINGTXTSHIFTOFFER as static final string type
	public static final String GETTINGTXTSHIFTOFFER = "co.legion.client.staging:id/tv_toolbar_title";
	// create variable of GETTINGTXTPROFILE as static final string type
	public static final String GETTINGTXTPROFILE = "co.legion.client.staging:id/title";
	// create variable of GETTINGTXTWORKPREFERENCES as static final string type
	public static final String GETTINGTXTWORKPREFERENCES = "//android.widget.TextView[@index='0']";
	// create variable of GETTINGTXTWHENIPREFER as static final string type
	public static final String GETTINGTXTWHENIPREFER = "co.legion.client.staging:id/whenIPreferToWork";
	// create variable of GETTINGTXTWHENBUSY as static final string type
	public static final String GETTINGTXTWHENBUSY = "co.legion.client.staging:id/whenIamBusy";
	// create variable of GETTINGTXTSETTINGS as static final string type
	public static final String GETTINGTXTSETTINGS = "co.legion.client.staging:id/tv_title";
	// create variable of GETTINGTXTFEEDBACK as static final string type
	public static final String GETTINGTXTFEEDBACK = "//android.widget.TextView[@text='Compose']";
	// create variable of GETTINGTXTPRIVACY as static final string type
	public static final String GETTINGTXTPRIVACY = "co.legion.client.staging:id/titletext";
	// create variable of GETTINGTXTTERM as static final string type
	public static final String GETTINGTXTTERM = "co.legion.client.staging:id/titletext";
	// create variable of GETTINGTXTONBOARDING as static final string type
	public static final String GETTINGTXTONBOARDING = "co.legion.client.staging:id/letStartedButton";	
	
	// constructor
	public NavigationsPageObjects(AppiumDriver driver) {
		this.d = driver;
	}

	// Verify the OnBoarding Screens navigation and verify the all navigation steps
	public String[] verifyTitleText(AppiumDriver d) {
		WebDriverWait wait = new WebDriverWait(d, 30);
		String text[]={"0","0","0","0","0"};
		try 
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTITLE)));
			text[0]=d.findElementById(GETTINGTXTTITLE).getText();
			System.out.println(text[0]);
			d.findElementById(NAVIGATIONNEXT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTITLE)));
			text[1]=d.findElementById(GETTINGTXTTITLE).getText();
			System.out.println(text[1]);
			d.findElementById(NAVIGATIONNEXT).click();
			d.findElementById(BTNGOTIT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTITLE)));
			text[2] = d.findElementById(GETTINGTXTTITLE).getText();
			System.out.println(text[2]);
			d.findElementById(NAVIGATIONNEXT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTITLE)));
			text[3]=d.findElementById(GETTINGTXTTITLE).getText();
			System.out.println(text[3]);
			d.findElementById(NAVIGATIONNEXT).click();
			text[4]=d.findElementById(GETTINGTXTTITLE).getText();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTITLE)));
			System.out.println(text[4]);
			d.findElementById(NAVIGATIONNEXT).click();
		} catch (Exception e) {
			e.getMessage();
			popUp(d);
		}
		
		return text;
	}
	//method for navigations
	public String verifyHome(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			screensht=Utilities.captureScreenshot(d, "Home1");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifySchedule(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			d.findElementByXPath(BTNSCHEDULE).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TILESTHIS_WEEK)));
			screensht=Utilities.captureScreenshot(d, "schedule1");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String VerifyShiftOffers(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{	
			d.findElementByXPath(BTNSHIFTOFFER).click();
			screensht=Utilities.captureScreenshot(d, "shift offers1");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifyMore(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			d.findElementByXPath(BTNMORE).click();
			screensht=Utilities.captureScreenshot(d, "more1");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifyProfile(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			d.findElementByXPath(LNKPROFILE).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(TXTBOXFIRSTNAME)));
			screensht=Utilities.captureScreenshot(d, "profile1");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifyAvailablity(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 30);
		String screensht=null;
		try
		{			
			d.findElementByXPath(BTNMORE).click();
			d.findElementByXPath(LNKAVAILABLITY).click();
			try
			{
				wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNGOTIT)));
				d.findElementById(BTNGOTIT).click();
			}
			catch(Exception e)
			{
				e.getMessage();
				popUp(d);
			}
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNWHENIMBUSY)));
			screensht=Utilities.captureScreenshot(d, "when i prefer to work1");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifyWhenImBusy(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			d.findElementById(BTNWHENIMBUSY).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNWHENIMBUSY)));
			screensht=Utilities.captureScreenshot(d, "when im busy1");
			//d.findElementById(BTNCANCEL).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifyWorkPreferences(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			d.findElementByXPath(BTNMORE).click();
			d.findElementByXPath(LNKWORKPREFERENCES).click();
			screensht=Utilities.captureScreenshot(d, "How i prefer to work1");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifyWhatsImportant(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			d.findElementById(BTNMYPRIORITIES).click();
			screensht=Utilities.captureScreenshot(d, "My Priorities1");
			//d.findElementById(BTNCANCEL).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifySettings(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			d.findElementByXPath(BTNMORE).click();
			d.findElementById(LNKSETTINGS).click();
			screensht=Utilities.captureScreenshot(d, "Notification settings1");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifyAboutLegion(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			d.findElementByXPath(BTNMORE).click();
			d.findElementById(LNKABOUTLEGION).click();
			screensht=Utilities.captureScreenshot(d, "About Legion1");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifyPrivacyPolicy(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			d.findElementById(LNKPRIVACYPOLICY).click();
			screensht=Utilities.captureScreenshot(d, "PrivacyPolicy");
			//d.findElementById(BTNCANCEL).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String verifyTermsOfUse(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		String screensht=null;
		try
		{
			d.findElementByXPath(BTNMORE).click();
			d.findElementById(LNKABOUTLEGION).click();
			d.findElementById(LNKTERMSOFUSE).click();
			screensht=Utilities.captureScreenshot(d, "TermsofUse");
			//d.findElementById(BTNCANCEL).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	
	public void closeSettings(AppiumDriver d)
	{
		try
		{
			d.findElementById(NAVIAGTIONTOOL_BAR).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	
	public void closeSetUp(AppiumDriver d)
	{
		try
		{
			d.findElementById(BTNCANCEL).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	public void verifyFeedback(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNHOME)));
			d.findElementByXPath(BTNMORE).click();
			d.findElementById(LNKSENDFEEDBACK).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		
	}
	
	public String feedbackNavigation(AppiumDriver d)
	{
		String screensht=null;
		try
		{
			key(d);	
			d.findElementByXPath(LNKEMAILMORE).click();
			screensht=Utilities.captureScreenshot(d, "FeedBack");
			d.findElementByXPath(LNKEMAILDISCARD).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public void verifyOnBoarding(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNHOME)));
			d.findElementByXPath(BTNMORE).click();
			d.scrollTo("On-Boarding");
			d.findElementByXPath(LNKONBOARDING).click();
		
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		
	}
	 
	public String onBoardingNavigations(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d,30);
		String screensht=null;
		try
		{
			d.findElementById(BTNLETSSTARTED).click();
			screensht=Utilities.captureScreenshot(d, "Boarding");
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTITLE)));
			d.findElementById(NAVIGATIONNEXT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTITLE)));
			d.findElementById(NAVIGATIONNEXT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTITLE)));
			d.findElementById(NAVIGATIONNEXT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTITLE)));
			d.findElementById(NAVIGATIONNEXT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTITLE)));
			d.findElementById(NAVIGATIONNEXT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNHOME)));
			d.findElementByXPath(BTNMORE).click();
			d.scrollTo("Logout");
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(LNKLOGOUT)));
			d.findElementByXPath(LNKLOGOUT).click();
			d.findElementById(BTNLOGOUT).click();
		}
		catch(Exception e){
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}	
	
	//Method for getting home screen title
	public String homeScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTHOME)));
			title=d.findElementById(GETTINGTXTHOME).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}
	
	//Method for getting schedule screen title
	public String scheduleScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTSCHEDULE)));
			title=d.findElementById(GETTINGTXTSCHEDULE).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

	//Method for getting shift offers screen title
	public String shiftOfferScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTSHIFTOFFER)));
			title=d.findElementById(GETTINGTXTSHIFTOFFER).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

	//Method for getting profile screen title
	public String myProfileScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTPROFILE)));
			title=d.findElementById(GETTINGTXTPROFILE).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

	//Method for getting wheniprefer screen title
	public String whenIPreferScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTWHENIPREFER)));
			title=d.findElementById(GETTINGTXTWHENIPREFER).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

	//Method for getting when im busy screen title
	public String whenImBusyScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTWHENBUSY)));
			title=d.findElementById(GETTINGTXTWHENBUSY).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

	//Method for getting how i prefer screen title
	public String howIPreferScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(GETTINGTXTWORKPREFERENCES)));
			title=d.findElementByXPath(GETTINGTXTWORKPREFERENCES).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

	//Method for getting whats important screen title
	public String whatsImportantScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(GETTINGTXTWORKPREFERENCES)));
			title=d.findElementByXPath(GETTINGTXTWORKPREFERENCES).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

	//Method for getting settings screen title
	public String settingsScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTSETTINGS)));
			title=d.findElementById(GETTINGTXTSETTINGS).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}
	
	//Method for getting privacy screen title
	public String privacyScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTPRIVACY)));
			title=d.findElementById(GETTINGTXTPRIVACY).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

	//Method for getting terms screen title
	public String termsScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTTERM)));
			title=d.findElementById(GETTINGTXTTERM).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

	//Method for getting Feedback screen title
	public String FeedbackScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(GETTINGTXTFEEDBACK)));
			title=d.findElementByXPath(GETTINGTXTFEEDBACK).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

	//Method for getting onboarding screen title
	public String onBoardingScreenTitle(AppiumDriver d)
	{
		String title=null;
		WebDriverWait wait = new WebDriverWait(d,30);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTONBOARDING)));
			title=d.findElementById(GETTINGTXTONBOARDING).getText();
			System.out.println(title);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return title;
	}

}

