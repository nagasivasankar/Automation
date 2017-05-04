package pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import mobileutilities.BaseClass;
import mobileutilities.Utilities;

public class ShiftOffersPageObjects extends BaseClass{

	public AppiumDriver d;
	// create variable for Shiftoffers button
	public static final String BTNSHIFTOFFR = "//android.widget.TextView[@text='Shift Offers']";
	// create variable for title unclaimed offers
	public static final String DOWN_ARROW = "co.legion.client.staging:id/iv_open_close_arrow";
	// create variable for title unclaimed offers
	public static final String TITLE_UNCLAIMEDOFFERS = "//android.widget.TextView[@text='Unclaimed Offers']";
	// create variable for title newoffers
	public static final String TITLE_NEWOFFERS = "//android.widget.TextView[@text='New Offers']";
	// create variable for title mypendingoffers
	public static final String TITLE_MYPENDINGOFFERS = "//android.widget.TextView[@text='My Pending Offers']";
	// create variable for title myapprovedoffers
	public static final String TITLE_MYAPPROVEDOFFERS = "//android.widget.TextView[@text='My Approved Offers']";
	// create variable for title myrejectedoffers
	public static final String TITLE_MYREJECTEDOFFERS = "//android.widget.TextView[@text='My Rejected Offers']";
	// create variable for title mybookmarks
	public static final String TITLE_MYBOOKMARKS = "//android.widget.TextView[@text='My Bookmarks']";
	// create variable for callouts in all screens
	public static final String CALLOUTS = "co.legion.client.staging:id/tv_call_outs";
	// create variable for swap in all screens
	public static final String SWAP = "co.legion.client.staging:id/tv_swaps";
	// create variable for openshifts in all screens
	public static final String OPENSHIFTS = "co.legion.client.staging:id/tv_open_shifts";
	// create variable for tiles in openshift offers screen
	public static final String TILEOPENSHIFT = "//android.widget.LinearLayout[@index='2']";
	// create variable for tiles in openshift offers screen
	public static final String TILEOPENSHIFT2 = "//android.widget.LinearLayout[contains(@resource-id,'co.legion.client.staging:id/parentLayout') and @text='1']";
	// create variable for shiftswap tiles
	public static final String TILESHIFTSWAP = "//android.widget.LinearLayout[@index='1']";
	// create variable for < in screen which is next of the shift swap &
	// openshift screens
	public static final String ARROWBACK = "co.legion.client.staging:id/toolbarBack";
	// create variable for decline button shift swap & openshift screens
	public static final String BTNDECLINE = "//android.widget.TextView=[@text='Decline']";
	// create variable for preview in schedule button shiftswap screen &
	// openshift screens
	public static final String BTNPREVIEWINSCHEDULE = "co.legion.client.staging:id/btn_preview_in_schedules";
	// create variable for preview in schedule button in shiftswap screen &
	// openshift screens
	public static final String BTNCLAIMTHISSHIFTOFFER = "//android.widget.Button[@index='9']";
	// create variable for storelead in openshiftscreen
	public static final String ICONSTORELEAD = "//android.widget.TextView[@text='Store Lead']";
	// create variable for storelead in openshiftscreen
	public static final String ICONDIRECTIONS = "co.legion.client.staging:id/tv_directions";
	// create variable for storelead in openshiftscreen
	public static final String ICONCONTACT = "co.legion.client.staging:id/tv_mobile_number";
	// create variable for cancel button in contact popup
	public static final String BTNCANCEL = "co.legion.client.staging:id/cancelTv";
	// create variable for cancel button in contact popup
	public static final String BTNCALL = "co.legion.client.staging:id/saveTv";
	// create variable for GOTIT in contact popup
	public static final String BTNGOTIT = "//android.widget.TextView=[@text='Got it']";
	// create variable for store lead button
	public static final String BTNSL = "co.legion.client.staging:id/text_linear_shift_lead";
	// create variable for close button
	public static final String BTNCLS = "co.legion.client.staging:id/closePopup";
	// create variable for call button
	public static final String BTNCAL = "co.legion.client.staging:id/tv_mobile_number";
	// create variable for cancel button
	public static final String BTNCANCL = "co.legion.client.staging:id/cancelTv";
	// create variable for down arrow
	public static final String BTNARROW = "co.legion.client.staging:id/expandCollapseImage";
	// create variable for I agree button
	public static final String BTNAGREE = "co.legion.client.staging:id/agreeTV";
	// create variable for Ok button
	public static final String BTNOK = "co.legion.client.staging:id/okTv";
	// create variable for decline button
	public static final String BTNDECLIN = "co.legion.client.staging:id/declineTv";
	// create variable for time conflict button
	public static final String BTNTC = "co.legion.client.staging:id/timeconflict_Iv";
	// create variable for location issue button
	public static final String BTNLI = "co.legion.client.staging:id/locationIv";
	// create variable for respond to requestr text field
	public static final String BTNRTR = "co.legion.client.staging:id/reason_Et";
	// create variable for bookmark symbol
	public static final String SYMBM = "co.legion.client.staging:id/bookmarkImage";
	// create variable for count
	public static final String TXTCOUNT = "co.legion.client.staging:id/tv_count";

	// constructor
	public ShiftOffersPageObjects(AppiumDriver driver) {
		this.d = driver;
	}

	// Verify the Unclaimed offers Count in Shift offer Screen
	public void unclaimedOperation(AppiumDriver d)
	{
		String screensht=null;
		try
		{
			WebDriverWait wait = new WebDriverWait(d, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSHIFTOFFR)));
			d.findElementByXPath(BTNSHIFTOFFR).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(DOWN_ARROW)));
			d.findElementById(DOWN_ARROW).click();
			screensht=Utilities.captureScreenshot(d, "list");
			d.findElementByXPath(TITLE_UNCLAIMEDOFFERS).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	
	public String unClaimedSwap(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d,30);
		String screensht=null;
		try 
		{
			d.findElementById(SWAP).click();
			d.findElementByXPath(TILESHIFTSWAP).click();
			screensht=Utilities.captureScreenshot(d, "shiftswap");
			d.findElementById(BTNDECLIN).click();
			d.findElementById(BTNTC).click();
			d.findElementById(BTNLI).click();
			d.findElementById(BTNRTR).clear();
			d.findElementById(BTNRTR).sendKeys("hi");
			d.findElementById(BTNDECLIN).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	
	public void unClaimedPending(AppiumDriver d)
	{
		WebDriverWait wait= new WebDriverWait(d,30);
		try
		{
			d.findElementById(BTNOK).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(DOWN_ARROW)));
			d.findElementById(DOWN_ARROW).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TITLE_MYPENDINGOFFERS)));
			d.findElementByXPath(TITLE_MYPENDINGOFFERS).click();
			d.findElementById(SWAP).click();
			d.findElementById(SWAP).getText();
			d.findElementById(TILESHIFTSWAP).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(DOWN_ARROW)));
			d.findElementById(DOWN_ARROW).click();
		} 
		catch (Exception e) {

			System.out.println("NO TILES IN SWAP");
			popUp(d);

		}
	}
	
	public void  unClaimedRejected(AppiumDriver d)
	{
		WebDriverWait wait= new WebDriverWait(d,30);
		String count=null;
		try {

			d.findElementByXPath(TILEOPENSHIFT).click();
			d.findElementById(BTNDECLIN).click();
			d.findElementById(BTNTC).click();
			d.findElementById(BTNLI).click();
			d.findElementById(BTNRTR).clear();
			d.findElementById(BTNRTR).sendKeys("hi");
			d.findElementById(BTNDECLIN).click();
			d.findElementById(BTNOK).click();
			d.findElementById(DOWN_ARROW).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TITLE_MYREJECTEDOFFERS)));
			d.findElementByXPath(TITLE_MYREJECTEDOFFERS).click();
			count=d.findElementById(TXTCOUNT).getText();
			System.out.println("total number rejectedoffers is :" +count );
			d.findElementById(OPENSHIFTS).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	
	public void unClaimedOpenShifts(AppiumDriver d)
	{
		WebDriverWait wait= new WebDriverWait(d,30);
		String screensht=null;
		try
		{
			d.findElementById(TILEOPENSHIFT).click();
			Utilities.captureScreenshot(d, "openshifts");
			wait.until(ExpectedConditions.elementToBeClickable(By.id(DOWN_ARROW)));
			d.findElementById(DOWN_ARROW).click();
		} 
		catch (Exception e) {

			System.out.println("NO TILES IN OPENSHIFTS ");
			popUp(d);
		}
	}

	// Verify the Unclaimed Offers navigation
	public void unclaimedoffers(AppiumDriver d) 
	{
		WebDriverWait wait = new WebDriverWait(d, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSHIFTOFFR)));
		d.findElementByXPath(BTNSHIFTOFFR).click();
		d.findElementById(DOWN_ARROW).click();
		d.findElementByXPath(TITLE_UNCLAIMEDOFFERS).click();

		try {
			d.findElementByXPath(TILEOPENSHIFT).click();
			d.findElementById(BTNDECLIN).click();
			d.findElementById(BTNTC).click();
			d.findElementById(BTNLI).click();
			d.findElementById(BTNRTR).clear();
			d.findElementById(BTNRTR).sendKeys("hi");
			d.findElementById(BTNCANCL).click();
			d.findElementById(BTNSL).click();
			d.findElementById(BTNCLS).click();
			d.findElementById(BTNCAL).click();
			d.findElementById(BTNCANCL).click();
			d.scrollTo("Preview in Schedule");
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNPREVIEWINSCHEDULE)));
			d.findElementById(BTNPREVIEWINSCHEDULE).click();
			d.findElementById(BTNARROW).click();
			d.findElementById(ARROWBACK).click();
			d.findElementByXPath(BTNCLAIMTHISSHIFTOFFER).click();
			d.findElementById(BTNCANCL).click();
			d.findElementById(SYMBM).click();
			d.findElementById(ARROWBACK).click();
		} catch (Exception e) {
			System.out.println("unclaimedoffers Tiles is empty");
			popUp(d);
		}
	}

	public String newOffersfunctionality(AppiumDriver d)
	{
		String screensht=null;
		try
		{
			WebDriverWait wait = new WebDriverWait(d, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSHIFTOFFR)));
			d.findElementByXPath(BTNSHIFTOFFR).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(DOWN_ARROW)));
			d.findElementById(DOWN_ARROW).click();
			screensht=Utilities.captureScreenshot(d, "list");
			d.findElementByXPath(TITLE_NEWOFFERS).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public void newOffersNavigations(AppiumDriver d)
	{
		WebDriverWait wait=new WebDriverWait(d,30);
		try {
			d.findElementByXPath(TILESHIFTSWAP).click();
			Utilities.captureScreenshot(d, "shiftswap");
			d.findElementById(BTNDECLIN).click();
			d.findElementById(BTNTC).click();
			d.findElementById(BTNLI).click();
			d.findElementById(BTNRTR).clear();
			d.findElementById(BTNRTR).sendKeys("hi");
			d.findElementById(BTNDECLIN).click();
			d.findElementById(BTNOK).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(DOWN_ARROW)));
			d.findElementById(DOWN_ARROW).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TITLE_MYPENDINGOFFERS)));
			d.findElementByXPath(TITLE_MYPENDINGOFFERS).click();
			d.findElementById(SWAP).click();
			d.findElementById(SWAP).getText();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	public void newOffer(AppiumDriver d)
	{
		WebDriverWait wait=new WebDriverWait(d,30);
		try
		{
			d.findElementById(TILESHIFTSWAP).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(DOWN_ARROW)));
			d.findElementById(DOWN_ARROW).click();
		} catch (Exception e) {
			System.out.println("NO SHIFTSWAP TILES IN NEWOFFERS");
			popUp(d);
		}
	}
	public void newOffersRejected(AppiumDriver d)
	{
		WebDriverWait wait= new WebDriverWait(d,30);
		d.findElementById(OPENSHIFTS).click();
		try {
			d.findElementByXPath(TILEOPENSHIFT).click();
			d.findElementById(BTNDECLIN).click();
			d.findElementById(BTNTC).click();
			d.findElementById(BTNLI).click();
			d.findElementById(BTNRTR).clear();
			d.findElementById(BTNRTR).sendKeys("hi");
			d.findElementById(BTNDECLIN).click();
			d.findElementById(BTNOK).click();
			d.findElementById(DOWN_ARROW).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TITLE_MYREJECTEDOFFERS)));
			d.findElementByXPath(TITLE_MYREJECTEDOFFERS).click();
			System.out.println("Total number rejectedoffers is :" + d.findElementById(TXTCOUNT).getText());
			d.findElementById(OPENSHIFTS).click();
			d.findElementById(OPENSHIFTS).getText();
			d.findElementById(TILEOPENSHIFT).click();
			Utilities.captureScreenshot(d, "openshifts");
			wait.until(ExpectedConditions.elementToBeClickable(By.id(DOWN_ARROW)));
			d.findElementById(DOWN_ARROW).click();
		} catch (Exception e) {
			System.out.println("NO OPENSHIFT TILES IN MYREJECTED OFFERS ");
			popUp(d);
		}
	}

	public void newOffersClaimed(AppiumDriver d)
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(d, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSHIFTOFFR)));
			d.findElementByXPath(BTNSHIFTOFFR).click();
			d.findElementById(DOWN_ARROW).click();
			d.findElementByXPath(TITLE_NEWOFFERS).click();
			d.findElementById(OPENSHIFTS).click();
			try 
			{
				d.findElementByXPath(TILEOPENSHIFT).click();
				d.findElementById(BTNDECLIN).click();
				d.findElementById(BTNTC).click();
				d.findElementById(BTNLI).click();
				d.findElementById(BTNRTR).clear();
				d.findElementById(BTNRTR).sendKeys("hi");
				d.findElementById(BTNCANCL).click();
				d.findElementById(BTNSL).click();
				d.findElementById(BTNCLS).click();
				d.findElementById(BTNCAL).click();
				d.findElementById(BTNCANCL).click();
				d.scrollTo("Preview in Schedule");
				wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNPREVIEWINSCHEDULE)));
				d.findElementById(BTNPREVIEWINSCHEDULE).click();
				Utilities.captureScreenshot(d, "PreviewinSchedule");
				d.findElementById(BTNARROW).click();
			}
			catch(Exception e)
			{
				e.getMessage();
				popUp(d);
			}
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	public void newOffersClaimedNavigation(AppiumDriver d)
	{
		WebDriverWait wait= new WebDriverWait(d,30);
		try
		{
			d.findElementById(ARROWBACK).click();
			d.findElementByXPath(BTNCLAIMTHISSHIFTOFFER).click();
			Utilities.captureScreenshot(d, "ClaimthisShiftOffer");
			d.findElementById(BTNAGREE).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNOK)));
			d.findElementById(BTNOK).click();
			d.findElementById(SYMBM).click();
			d.findElementById(ARROWBACK).click();
		} catch (Exception e) {
			System.out.println("NO OPENSHIFT TILES IN NEWOFFERS");
			popUp(d);

		}
	}

	public void myPendingOffers(AppiumDriver d) 
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(d, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSHIFTOFFR)));
			d.findElementByXPath(BTNSHIFTOFFR).click();
			d.findElementById(DOWN_ARROW).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TITLE_MYPENDINGOFFERS)));
			d.findElementByXPath(TITLE_MYPENDINGOFFERS).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	public void  myPendingswap(AppiumDriver d)
	{
		try{
			WebDriverWait wait = new WebDriverWait(d, 10);
			d.findElementById(SWAP).click();
			d.findElementById(TILESHIFTSWAP).click();
			d.findElementById(ARROWBACK).click();
			
		} 
		catch (Exception e)
		{
			System.out.println("NO SHIFTSWAP TILES IN MYPENDING OFFERS");
			popUp(d);
		}
			
		}
	public void myPendingOffersNavigations(AppiumDriver d)
	{
		WebDriverWait wait=new WebDriverWait(d,30);
		try {
			d.findElementById(OPENSHIFTS).click();
			d.findElementByXPath(TILEOPENSHIFT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNSL)));
			d.findElementById(BTNSL).click();
			d.findElementById(BTNCLS).click();
			d.findElementById(BTNCAL).click();
			d.findElementById(BTNCANCL).click();
			d.findElementById(SYMBM).click();
			d.findElementById(ARROWBACK).click();
		} catch (Exception e) {
			System.out.println("NO OPENSHIFT TILES IN MYPENDING OFFERS");
			popUp(d);
		}
	}

	public void myApprovedOffers(AppiumDriver d)
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(d, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSHIFTOFFR)));
			d.findElementById(DOWN_ARROW).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TITLE_MYAPPROVEDOFFERS)));
			d.findElementByXPath(TITLE_MYAPPROVEDOFFERS).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	public void  myApprovedswap(AppiumDriver d)
	{
		try{
			WebDriverWait wait = new WebDriverWait(d, 10);
			d.findElementById(SWAP).click();
			d.findElementById(TILESHIFTSWAP).click();
			d.findElementById(ARROWBACK).click();
			
		} 
		catch (Exception e)
		{
			System.out.println("NO SHIFTSWAP TILES IN MYAPPROVED OFFERS");
			popUp(d);
		}
			
		}
	public void myApprovedOffersNavigations(AppiumDriver d)
	{
		WebDriverWait wait=new WebDriverWait(d,30);
		try {
			d.findElementById(OPENSHIFTS).click();
			d.findElementByXPath(TILEOPENSHIFT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNSL)));
			d.findElementById(BTNSL).click();
			d.findElementById(BTNCLS).click();
			d.findElementById(BTNCAL).click();
			d.findElementById(BTNCANCL).click();
			d.findElementById(SYMBM).click();
			d.findElementById(ARROWBACK).click();
		} catch (Exception e) {
			System.out.println("NO TILES IN MYAPPROVED OFFERS");
			popUp(d);

		}
	}

	public void myRejectedOffers(AppiumDriver d) 
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(d, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSHIFTOFFR)));
			d.findElementById(DOWN_ARROW).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TITLE_MYREJECTEDOFFERS)));
			d.findElementByXPath(TITLE_MYREJECTEDOFFERS).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	public void  myRejectedswap(AppiumDriver d)
	{
		try{
			WebDriverWait wait = new WebDriverWait(d, 10);
			d.findElementById(SWAP).click();
			d.findElementById(TILESHIFTSWAP).click();
			d.findElementById(ARROWBACK).click();
			
		} 
		catch (Exception e)
		{
			System.out.println("NO SHIFTSWAP TILES IN MYREJECTED OFFERS");
			popUp(d);
		}
			
		}
	
	public void myRejectedOffersNavigation(AppiumDriver d)
	{
		WebDriverWait wait=new WebDriverWait(d,30);
		try {
			d.findElementById(OPENSHIFTS).click();
			d.findElementByXPath(TILEOPENSHIFT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNSL)));
			d.findElementById(BTNSL).click();
			d.findElementById(BTNCLS).click();
			d.findElementById(BTNCAL).click();
			d.findElementById(BTNCANCL).click();
			d.findElementById(SYMBM).click();
			d.findElementById(ARROWBACK).click();

		} 
		catch (Exception e) 
		{
			System.out.println("NO OPENSHIFT TILES IN MYREJECTED OFFERS");
			popUp(d);
		}
	}

	public void myBookmarks(AppiumDriver d)
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(d, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSHIFTOFFR)));
			d.findElementById(DOWN_ARROW).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TITLE_MYBOOKMARKS)));
			d.findElementByXPath(TITLE_MYBOOKMARKS).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
	}
	public String callOutsCount(AppiumDriver d)
	{
		String callout=null;
		try
		{
			callout=d.findElementById(CALLOUTS).getText();
			System.out.println("Total number of callouts in MyBookmarks is :" + callout);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return callout;
	}
	public String swapCount(AppiumDriver d)
	{
		String swap=null;
		try
		{		
			swap=d.findElementById(SWAP).getText();		
			System.out.println("Total number of swaps in MyBookmarks is :" + swap);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return swap;
	}
	
	public String openShiftsCount(AppiumDriver d)
	{
		String openshift=null;
		try
		{
			openshift=d.findElementById(OPENSHIFTS).getText();
			System.out.println("Total number of openshifts in  MyBookmarks is :" + openshift);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return openshift;
	}
	public String calloutScreen(AppiumDriver d)
	{
		String screensht=null;
		WebDriverWait wait = new WebDriverWait(d, 10);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(CALLOUTS)));
			d.findElementById(CALLOUTS).click();
			screensht=Utilities.captureScreenshot(d, "callouts");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String swapScreen(AppiumDriver d)
	{
		String screensht=null;
		WebDriverWait wait = new WebDriverWait(d, 10);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(SWAP)));
			d.findElementById(SWAP).click();
			screensht=Utilities.captureScreenshot(d, "shiftswap");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public String openShiftScreen(AppiumDriver d)
	{
		String screensht=null;
		WebDriverWait wait = new WebDriverWait(d, 10);
		try
		{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(OPENSHIFTS)));
			d.findElementById(OPENSHIFTS).click();
			screensht=Utilities.captureScreenshot(d, "Openshifts");
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return screensht;
	}
	public void  myBookmarksswap(AppiumDriver d)
	{
		try{
			WebDriverWait wait = new WebDriverWait(d, 10);
			d.findElementById(SWAP).click();
			d.findElementById(TILESHIFTSWAP).click();
			d.findElementById(ARROWBACK).click();
			
		} 
		catch (Exception e)
		{
			System.out.println("NO SHIFTSWAP TILES IN MYBOOKMARKS OFFERS");
			popUp(d);
		}
			
		}
	public void myBookMarksNavigation(AppiumDriver d)
	{
		
		WebDriverWait wait=new WebDriverWait(d,30); 
		try {
			d.findElementById(OPENSHIFTS).click();
			d.findElementByXPath(TILEOPENSHIFT).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNSL)));
			d.findElementById(BTNSL).click();
			d.findElementById(BTNCLS).click();
			d.findElementById(BTNCAL).click();
			d.findElementById(BTNCANCL).click();
			d.findElementById(SYMBM).click();
			d.findElementById(ARROWBACK).click();
		} catch (Exception e) {
			System.out.println("NO OPENSHIFT TILES IN MY BOOKMARKS");
			popUp(d);
		}
	}

}
