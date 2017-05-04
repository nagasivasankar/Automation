package pageobjects;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import mobileutilities.Utilities;

public class WhenIPrefertoWorkPageObjects extends mobileutilities.BaseClass {

	//create driver 
		public AppiumDriver d;
		//create variable of GETTINGTXTNUMHRS as static final string type
		public static final String GETTINGTXTNUMHRS= "co.legion.client.staging:id/numOfAvailableHrsTV";
		//create variable of GETTINGTXTHEADERDATE as static final string type
		public static final String GETTINGTXTHEADERDATE = "co.legion.client.staging:id/headerDateTv";
		//create variable of TIMEBLOCKHRS as static final string type
		public static final String TIMEBLOCKHRS = "//android.widget.TextView[@text='8pm - 12am']";
		//create variable of TIMEBLOCKHRS1 as static final string type
		public static final String TIMEBLOCKHRS1 = "//android.widget.TextView[@text='6am-8am']";
		//create variable of GETTINGTEXT as static final string type
		public static final String GETTINGTXT= "//android.widget.TextView[@text='High availability = high chance of getting preferred work hrs/wk']";
		//create variable of LNKAVAILABLITY as static final String type
		public static final String LNKAVAILABLITY="co.legion.client.staging:id/availability";
		//create variable of BTNWHENIMBUSY as static final String type
		public static final String BTNWHENIBUSY="co.legion.client.staging:id/whenIamBusy";
		//create variable of BTNWHENIPREFER as static final String type
		public static final String BTNWHENIPREFER="co.legion.client.staging:id/whenIPreferToWork";
		//create variable of GETTINGTXTTITLE as static final String type
		public static final String GETTINGTXTTITLE="co.legion.client.staging:id/title";
		//create variable of BTNMORE as static final string type
		public static final String BTNMORE="//android.widget.TextView[@text='More']";
		//create variable of CLICKDAY as static final string type
		public static final String CLICKDAY="co.legion.client.staging:id/tv_wednesday";
		//create variable of CLICKDAY1 as static final string type
		public static final String CLICKDAY1="co.legion.client.staging:id/tv_thursday";
		//create variable of BTNAPPLY as static final string type
		public static final String BTNAPPLY="co.legion.client.staging:id/tv_apply";
		//create variable of BTNCANCEL as static final string type
		public static final String BTNCANCEL="co.legion.client.staging:id/tv_cancel";
		//create variable of NAVIGATIONOK as static final string type
		public static final String NAVIGATIONOK="co.legion.client.staging:id/saveAvailability";
		//create variable of button BTNGOTIT as static final string type
		public static final String BTNGOTIT="co.legion.client.staging:id/gotItTv";
		//create variable of IMAGELOCK as static final string type
		public static final String IMAGELOCK = "co.legion.client.staging:id/lockImage";
		//create variable of NAVIGATIONPREV as static final string type
		public static final String NAVIGATIONPREV = "co.legion.client.staging:id/prevIv";
		//create variable of NAVIGATIONNEXT as static final string type
		public static final String NAVIGATIONNEXT = "co.legion.client.staging:id/nextIv";
		//create variable of BTNOK as static final string type
		public static final String BTNOK = "co.legion.client.staging:id/okTV";
		//create variable of NAVIGATIONCANCEL as static final string type
		public static final String NAVIGATIONCANCEL= "co.legion.client.staging:id/closeSetup";
		//create variable of CHKBOXAPPLY as static final string type
		public static final String CHKBOXAPPLY= "co.legion.client.staging:id/repeatImage";
		//create variable of NAVIGATIONCANCEL1 as static final string type
		public static final String NAVIGATIONCANCEL1= "co.legion.client.staging:id/cancelButton";
		//create variable of NAVIGATIONDISCARD as static final string type
		public static final String NAVIGATIONDISCARD= "co.legion.client.staging:id/discardChangesButton";
		//create variable of NAVIGATIONSAVE as static final string type
		public static final String NAVIGATIONSAVE= "co.legion.client.staging:id/saveChangesButton";
		//create variable of NAVIGATIONTHISWEEK as static final string type
		public static final String NAVIGATIONTHISWEEK= "co.legion.client.staging:id/thisWeekOnlyButton";
		//create variable of NAVIGATIONREPEATFORWARD as static final string type
		public static final String NAVIGATIONREPEATFORWARD= "co.legion.client.staging:id/repeatForwardButton";
		//create variable of BTNCUSTOMIZE as static final string type
		public static final String BTNCUSTOMIZE= "co.legion.client.staging:id/customSlotLL";
		//create variable of BTNDELETE as static final string type
		public static final String BTNDELETE= "co.legion.client.staging:id/deleteLL";
		//create variable of OPTIONJ as static final string type
		public static final String OPTIONJ= "co.legion.client.staging:id/j_options1";
		//create variable of BTNCANCEL as static final string type
		public static final String BTNCLOSESETUP = "co.legion.client.staging:id/closeSetup";
		//constructor
		public WhenIPrefertoWorkPageObjects(AppiumDriver driver)
		{
			this.d=driver;
		}

		//method for when i prefer to work
		public String whenIPreferToWork(AppiumDriver d)
		{
			WebDriverWait wait = new WebDriverWait(d,30);
			try
			{
				d.findElementById(BTNCLOSESETUP).click();
			}
			catch(Exception e)
			{
				e.getMessage();
				popUp(d);
			}	
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNMORE)));
			d.findElementByXPath(BTNMORE).click();
			d.findElementById(LNKAVAILABLITY).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNGOTIT)));
			d.findElementById(BTNGOTIT).click();			
			String screensht = Utilities.captureScreenshot(d, "When I Prefer");
			return screensht;
		}
		public String verifyPageTitle(AppiumDriver d)
		{	
			String pagetitle=null;
			try
			{
				pagetitle=d.findElementById(GETTINGTXTTITLE).getText();
				System.out.println("Title of Screen:"+pagetitle);
				return pagetitle;
			}
			catch(Exception e)
			{
				e.getMessage();
				popUp(d);
			}
			return pagetitle;
		}
		public String verifyhourstext(AppiumDriver d)
		{
			String hourstext=null;
			try
			{
				hourstext=d.findElementByXPath(GETTINGTXT).getText()+"   "+d.findElementById(WhenIPrefertoWorkPageObjects.GETTINGTXTNUMHRS).getText();
				System.out.println("Total working hours before selecting the work:"+hourstext);
			}
			catch(Exception e)
			{
				e.getMessage();
				popUp(d);
			}
			return hourstext;
		}
		public String verifyWeekDay(AppiumDriver d)
		{
			String dayWeek= "0";
			WebDriverWait wait = new WebDriverWait(d,30);
			try
			{

				dayWeek=d.findElementById(GETTINGTXTHEADERDATE).getText();
				try
				{
					WebElement element= d.findElementById(IMAGELOCK);
					if(element.isEnabled())
					{
						d.findElementById(NAVIGATIONNEXT).click();
						d.findElementById(NAVIGATIONCANCEL).click();
					}
				}
				catch(Exception e)
				{
					e.getMessage();
					d.findElement(By.xpath(TIMEBLOCKHRS)).click();
					d.findElementById(BTNCUSTOMIZE).click();
					d.findElementById(OPTIONJ).click();
					d.findElementById(CLICKDAY).click();
					d.findElementById(CLICKDAY1).click();
					d.findElementById(BTNAPPLY).click();
					System.out.println("Header Week : " +dayWeek);
					d.findElementById(NAVIGATIONCANCEL).click();
					d.findElementById(CHKBOXAPPLY).click();
					d.findElementById(NAVIGATIONCANCEL1).click();
					d.findElementById(NAVIGATIONOK).click();
					d.findElementById(NAVIGATIONCANCEL1).click();
					d.findElementById(NAVIGATIONOK).click();
					d.findElementById(NAVIGATIONTHISWEEK).click();
					popUp(d);
					
				} 
				d.findElementById(NAVIGATIONCANCEL).click();
			}
			catch(Exception e)
			{
				
				e.getMessage();
				popUp(d);
			}
			return dayWeek;
		}

}
