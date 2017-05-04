package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import mobileutilities.Utilities;

public class ScheduleOverviewPageObjects {
	//create driver 
	public AppiumDriver d;
	//create variable of schedule as static final string type
	public static final String BTNSCHEDULE = "//android.widget.TextView[@text='Schedule']";
	//create variable of more as static final string type
	public static final String BTNMORE="//android.widget.TextView[@text='More']";
	//create variable of past week as static final string type
	public static final String TILESPAST_WEEK="//android.widget.TextView[@text='  PAST WEEK']";
	//create variable of this week as static final string type
	public static final String TILESTHIS_WEEK="//android.widget.TextView[@text='  THIS WEEK']";
	//create variable of final week as static final string type
	public static final String TILESFINAL_WEEK="//android.widget.TextView[@text='  FINALIZED']";
	//create variable of final week as static final string type
	public static final String TILESPUBLISHED_WEEK="//android.widget.TextView[@text='  PUBLISHED']";
	//create variable of final week as static final string type
	public static final String TILESFUTURE_WEEK="co.legion.client.staging:id/scheduleWeekTv";
	//create variable of schedule title as static final string type
	public static final String SCHEDULE_TITLE="co.legion.client.staging:id/tv_title";
	//create variable of date title as static final string type
	public static final String DATE_TITLE="co.legion.client.staging:id/dateTv";
	//create variable of title_name as static final string type
	public static final String TITLE_NAME="co.legion.client.staging:id/nameTV";
	//create variable of prev as static final string type
	public static final String NAVIGATIONPREV="co.legion.client.staging:id/prevIv";
	//create variable of next as static final string type
	public static final String NAVIGATIONNEXT="co.legion.client.staging:id/nextIv";
	//create variable of tool bar as static final string type
	public static final String NAVIAGTIONTOOL_BAR="co.legion.client.staging:id/toolbarBack";
	//create variable of summary as static final string type
	public static final String LNKSUMMARY="co.legion.client.staging:id/expandCollapseImage";
	//create variable of summary as static final string type
	public static final String TIME_INTERVAL="co.legion.client.staging:id/tv_time_interval";
	//create variable of address as static final string type
	public static final String GETTINGTXTADDRESS="co.legion.client.staging:id/tv_address";
	//create variable of peak as static final string type
	public static final String GETTINGTXTPEAK="co.legion.client.staging:id/tv_peak_hours";
	//create variable of peak as static final string type
	public static final String BTNDIRECTIONS="co.legion.client.staging:id/tv_directions";
	//create variable of peak as static final string type
	public static final String BTNCLOSE="com.google.android.apps.maps:id/search_omnibox_text_clear";
	//create variable of peak as static final string type
	public static final String GETTINGTXTAPPROVED = "co.legion.client.staging:id/offerAppTv";
	//create variable of peak as static final string type
	public static final String BTNOFFERYOURSHIFT="co.legion.client.staging:id/btn_offer_your_shift";
	//create variable of peak as static final string type
	public static final String GETTINGTXTTITLEOFFERYOURSHIFT="co.legion.client.staging:id/tv_title";
	//create variable of peak as static final string type
	public static final String NAVIGATIONSWAPYOURSHIFT="//android.widget.TextView[@text='Request to Swap your Shift']";	
	//create variable of peak as static final string type
	public static final String NAVIGATIONDROPYOURSHIFT="//android.widget.TextView[@text='Request to Drop your Shift']";	
	//create variable of peak as static final string type
	public static final String BTNBACK="co.legion.client.staging:id/btBackTv";
	//create variable of peak as static final string type
	public static final String BTNNEXT="co.legion.client.staging:id/btNextTv";
	//create variable of peak as static final string type
	public static final String CHXBOXSELECT="co.legion.client.staging:id/selectedIv";
	//create variable of peak as static final string type
	public static final String BTNCLOSESHIFT="co.legion.client.staging:id/closeShiftIv";
	//create variable of peak as static final string type
	public static final String GETTINGTXTSUCCESS="//android.widget.TextView[@text='Your Swap Request was successfully submitted!']";
	//create variable of peak as static final string type
	public static final String BTNRETURN="co.legion.client.staging:id/returnBt";
	//create variable of peak as static final string type
	public static final String BTNCANCELSHIFT="co.legion.client.staging:id/btnCancelShift";
	//create variable of peak as static final string type
	public static final String BTNCANCELSWAP="co.legion.client.staging:id/cancelSwapTv";
	//create variable of peak as static final string type
	public static final String BTNOK="co.legion.client.staging:id/okTv";
	//create variable of peak as static final string type
	public static final String BTNCANCEL="co.legion.client.staging:id/cancelTv";
	//create variable of peak as static final string type
	public static final String BTNCLOSEPOPUP="co.legion.client.staging:id/closePopup";
	//create variable of peak as static final string type
	private static final String BTNSTORELEAD ="co.legion.client.staging:id/text_linear_shift_lead";	
	//create variable of peak as static final string type
	private static final String BTNMOBILE ="co.legion.client.staging:id/tv_mobile_number";	
	//create variable of NAIGATIONCANCEL as static final string type
	public static final String NAVIGATIONCANCEL= "co.legion.client.staging:id/closeSetup";
	//create variable of BTNLETSSTARTED as static final string type
	public static final String BTNLETSSTARTED = "co.legion.client.staging:id/letStartedButton";
	//create variable of drop button as static final string type
	public static final String BTNDROPSHIFTREQ="//android.widget.TextView[@text='Request to Drop your Shift']";
	//create variable of peak as static final string type
		public static final String BTNRETRN="//android.widget.TextView[@text='Return to My Schedule']";
		
	
	//constructor
	public ScheduleOverviewPageObjects(AppiumDriver driver)
	{
		this.d=driver;
	}
    //method for summary
	public void testSummary(AppiumDriver d){
	    	try{
				
				d.findElementById(LNKSUMMARY).click();
				d.findElementById(LNKSUMMARY).click();
	    	}catch(Exception e)
			{
				e.getMessage();
			}
	    	 }
		public String testTitle(AppiumDriver d) 
		{
			String scrfilename ="screenshot not taken";
			WebDriverWait wait = new WebDriverWait(d,120);
			try
			{
				d.findElementById(TITLE_NAME).click();
				scrfilename=Utilities.captureScreenshot(d, "assign shift details");
			}catch(Exception e)
			{
				e.getMessage();
			}
			return scrfilename;
		}
	    
		 public void testScheduleTitle(AppiumDriver d){
		    	try{
					
		    		System.out.println("Title for schedule overview screen is : "+d.findElementById(SCHEDULE_TITLE).getText());
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 //method for text in the tile
		 public void testScheduleText(AppiumDriver d){
		    	try{
					
		    		System.out.println("Title for schedule overview screen is : "+d.findElementById(GETTINGTXTAPPROVED).getText());
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 //method for time in the tile
		 public void testScheduleTime(AppiumDriver d){
		    	try{
					
		    		System.out.println("Title for schedule overview screen is : "+d.findElementById(TIME_INTERVAL).getText());
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 //method for address in the tile
		 public void testScheduleAddress(AppiumDriver d){
		    	try{
					
		    		System.out.println("Title for schedule overview screen is : "+d.findElementById(GETTINGTXTADDRESS).getText());
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 //method forstorelead in the tile
		 public void testStoreLead(AppiumDriver d){
		    	try{
					
		    		d.findElementById(BTNSTORELEAD).click();
		    		d.findElementById(BTNCLOSEPOPUP).click();
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 //method for calling button in the tile
		 public void testCalling(AppiumDriver d){
		    	try{
					
		    		d.findElementById(BTNMOBILE).click();
					d.findElementById(BTNCANCEL).click();
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 //method for directions in the tile
		 public void testDirections(AppiumDriver d){
				WebDriverWait wait = new WebDriverWait(d,120);
			
		    	try{
					
		    		d.findElementById(BTNDIRECTIONS).click();
					wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNCLOSE)));
					Utilities.captureScreenshot(d,"directions");
					((AndroidDriver)d).startActivity("co.legion.client.staging", "co.legion.client.activities.LegionSplashActivity");
		    	}catch(Exception e)
				{
					e.getMessage();
				}
	 }
		 //method for letsget started in the dashboard
		 public void testletsStarted(AppiumDriver d){
			 WebDriverWait wait = new WebDriverWait(d,120);
			 try
				{
					wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNLETSSTARTED)));
					d.findElementById(BTNLETSSTARTED).click();
					d.findElementById(NAVIGATIONCANCEL).click();
				}
				catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 public void testSchedule(AppiumDriver d){
				WebDriverWait wait = new WebDriverWait(d,120);
		    	try{
		    		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSCHEDULE)));
					d.findElementByXPath(BTNSCHEDULE).click();
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 public void testScheduleTile(AppiumDriver d){
				WebDriverWait wait = new WebDriverWait(d,120);
		    	try{
		    		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TILESTHIS_WEEK)));
					d.findElementByXPath(TILESTHIS_WEEK).click();
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 //method to navigate to previous tile
		 public void testSchedulePrevNavigation(AppiumDriver d){
				WebDriverWait wait = new WebDriverWait(d,120);
		    	try{
		    		wait.until(ExpectedConditions.elementToBeClickable(By.id(NAVIGATIONPREV)));
					d.findElementById(NAVIGATIONPREV).click();
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 
		 public void testScheduleTitleName(AppiumDriver d){
			
		    	try{
		    		d.findElementById(TITLE_NAME).click();
					//Utilities.captureScreenshot(d,"WEEK TITLE");
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		 //method for navigation back
		 public void testScheduleBackNavigation(AppiumDriver d){
			 WebDriverWait wait = new WebDriverWait(d,120);
		    	try{
		    		wait.until(ExpectedConditions.elementToBeClickable(By.id(NAVIAGTIONTOOL_BAR)));
					d.findElementById(NAVIAGTIONTOOL_BAR).click();
		    	}catch(Exception e)
				{
					e.getMessage();
				}
		    	 }
		// Verify the Past schedule in schedule overview screen
			public void pastScheduleNavigation(AppiumDriver d) 
			{
				WebDriverWait wait = new WebDriverWait(d,120);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSCHEDULE)));
				d.findElementByXPath(BTNSCHEDULE).click();
				Utilities.captureScreenshot(d, "schedule overview");
				System.out.println("Title for schedule overview screen is : "+d.findElementById(SCHEDULE_TITLE).getText());
				d.findElementByXPath(BTNSCHEDULE).click();
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TILESTHIS_WEEK)));
				d.findElementByXPath(TILESTHIS_WEEK).click();
				wait.until(ExpectedConditions.elementToBeClickable(By.id(NAVIGATIONPREV)));
				d.findElementById(NAVIGATIONPREV).click();
				wait.until(ExpectedConditions.elementToBeClickable(By.id(SCHEDULE_TITLE)));
				System.out.println("Title for schedule overview screen is : "+d.findElementById(SCHEDULE_TITLE).getText());
				System.out.println("Title for schedule overview screen is : "+d.findElementById(DATE_TITLE).getText());
				Utilities.captureScreenshot(d, "Past schedule");

			}	
			    
		
	// Verify the Current Schedules in schedule over screen
	public void currentScheduleNavigation(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d,120);
		try
		{
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSCHEDULE)));
		d.findElementByXPath(BTNSCHEDULE).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TILESTHIS_WEEK)));
		d.findElementByXPath(TILESTHIS_WEEK).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id(SCHEDULE_TITLE)));
		System.out.println("Title for schedule overview screen is : "+d.findElementById(SCHEDULE_TITLE).getText());
		System.out.println("Title for schedule overview screen is : "+d.findElementById(DATE_TITLE).getText());
		Utilities.captureScreenshot(d, "current schedule");
		}catch(Exception e)
		{
			e.getMessage();
		}
}	
		
	// Verify the Finalized Schedules in schedule overview screen
	public void finalizedScheduleNavigation(AppiumDriver d)
	{
		WebDriverWait wait=new WebDriverWait(d,60);
		try{
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNSCHEDULE)));
		d.findElementByXPath(BTNSCHEDULE).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TILESFINAL_WEEK)));
		d.findElementByXPath(TILESFINAL_WEEK).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id(SCHEDULE_TITLE)));
		System.out.println("Title for schedule overview screen is : "+d.findElementById(SCHEDULE_TITLE).getText());
		System.out.println("Title for schedule overview screen is : "+d.findElementById(DATE_TITLE).getText());
		
		Utilities.captureScreenshot(d, "Finalized schedule");
	}catch(Exception e)
	{
		e.getMessage();
	}
	}	
		
	 // Verify Published Schedules in Schedule overview Screen
	public void publishedScheduleNavigation(AppiumDriver d)
	{
		WebDriverWait wait=new WebDriverWait(d,60);
		try{
		d.findElementByXPath(BTNSCHEDULE).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TILESPUBLISHED_WEEK)));
		d.findElementByXPath(TILESPUBLISHED_WEEK).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id(SCHEDULE_TITLE)));
		System.out.println("Title for schedule overview screen is : "+d.findElementById(SCHEDULE_TITLE).getText());
		System.out.println("Title for schedule overview screen is : "+d.findElementById(DATE_TITLE).getText());
		Utilities.captureScreenshot(d, "published schedule");
		}
		catch(Exception e)
		{
			e.getMessage();
		}
			}	
		
	//Verify the Shift Offers 
	public void shiftOffer(AppiumDriver d)
	{
		try{
		WebDriverWait wait=new WebDriverWait(d,120);
		d.findElementById(BTNOFFERYOURSHIFT).click();
		System.out.println(d.findElementById(GETTINGTXTTITLEOFFERYOURSHIFT).getText());
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(NAVIGATIONSWAPYOURSHIFT)));
		d.findElementByXPath(NAVIGATIONSWAPYOURSHIFT).click();
		try
		{
			d.findElementById(CHXBOXSELECT).click();
			d.findElementById(BTNNEXT).click();
			d.findElementById(BTNNEXT).click();
			System.out.println(d.findElementByXPath(GETTINGTXTSUCCESS).getText());
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNRETRN)));
			d.findElementByXPath(BTNRETRN).click();
		
		}
		catch(Exception e)
		{
			d.findElementById(BTNBACK).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNDROPSHIFTREQ)));
			 d.findElementByXPath(BTNDROPSHIFTREQ).click();
			 d.findElementById(BTNBACK).click();
			d.findElementById(NAVIAGTIONTOOL_BAR).click();
		
			e.getMessage();
		}
		}catch(Exception e1)
		{
			e1.getMessage();
		}
	}
	//Verify the Shift Offers Cancellation
	public void shiftCancel(AppiumDriver d)
	{
		try
		{
			WebDriverWait wait=new WebDriverWait(d,60);
			d.scrollTo("Cancel your Swap Request");
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNCANCELSHIFT)));
			d.findElementById(BTNCANCELSHIFT).click();
			d.findElementById(BTNCANCELSWAP).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNOK)));
			d.findElementById(BTNOK).click();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
}
