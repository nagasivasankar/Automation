package pageobjects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import mobileutilities.BaseClass;
import mobileutilities.Utilities;

public class HomeScreenPageObjects extends BaseClass
{

	//create driver 
	public AppiumDriver d;
	//create variable of gettingtxtwish as static final string type
	public static final String GETTINGTXTWISH = "co.legion.client.staging:id/dayWishesTV";
	//create variable of gettingtxtday as static final string type
	public static final String GETTINGTXTDAY = "co.legion.client.staging:id/todayTV";
	//create variable of gettingtxtshift as static final string type
	public static final String GETTINGTXTSHIFT = "co.legion.client.staging:id/shift_day_with_time";
	//create variable of gettingtxtCURRENT DAY as static final string type
	public static final String GETTINGTXTCURRENTDAY = "co.legion.client.staging:id/currentDayTV";
	//create variable of gettingtxtshift as static final string type
	public static final String GETTINGTXTSHIFT1 = "co.legion.client.staging:id/noShiftsTV";
	//create variable of gettingtxtschedulecount as static final string type
	public static final String GETTINGTXTSCHEDULECOUNT = "co.legion.client.staging:id/schedule_count_tv";
	//create variable of gettingtxtschedule as static final string type
	public static final String GETTINGTXTSCHEDULE = "//android.widget.TextView[@text='Schedules']";
	//create variable of gettingtxtshiftoffercount as static final string type
	public static final String GETTINGTXTSHIFTOFFERCOUNT = "co.legion.client.staging:id/shift_offers_count_tv";
	//create variable of gettingtxtshiftoffer as static final string type
	public static final String GETTINGTXTSHIFTOFFER = "//android.widget.TextView[@text='Shift Offers']";
	//create variable of BTNHOME as static final string type
	public static final String BTNHOME = "//android.widget.TextView[@text='Home']";
	//create variable of navigationtool_bar as static final string type
	public static final String NAVIAGTIONTOOL_BAR="co.legion.client.staging:id/toolbarBack";
	//create variable of BTNCANCEL as static final string type
	public static final String BTNCANCEL = "co.legion.client.staging:id/closeSetup";
	
	//constructor
	public HomeScreenPageObjects(AppiumDriver driver)
	{
		this.d=driver;
	}
	
	//Mehtod for home screen in the app after login
	public String verifyDate(AppiumDriver d)
	{	
		String daytext = d.findElementById(GETTINGTXTDAY).getText();
		DateFormat dateFormat = new SimpleDateFormat("EEEEEE, MMM dd");
		Date dt = new Date();
		String str=dateFormat.format(dt);
		String str22=daytext;
		System.out.println("Str : "+str);
		String sub = daytext.substring(5,str22.length()-3);
		System.out.println("Sub : "+sub);
		boolean result= str.equalsIgnoreCase(sub);
		System.out.println(result);
		return daytext;
	}
	//Method for navigation
	public String[] navigations(AppiumDriver d)
	{
		String scrfilename[]={"0","0","0"}; 
		d.findElementByXPath(GETTINGTXTSCHEDULE).click();
		scrfilename[0]=Utilities.captureScreenshot(d,"Scheduleoverview");
		d.findElementByXPath(BTNHOME).click();
		d.findElementByXPath(BTNHOME).click();
		d.findElementByXPath(GETTINGTXTSHIFTOFFER).click();
		scrfilename[1]=Utilities.captureScreenshot(d, "Shift offers");
		d.findElementByXPath(BTNHOME).click();
		try
		{
			WebDriverWait wait = new WebDriverWait(d, 60);
			wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTSHIFT)));
			d.findElementById(GETTINGTXTSHIFT).click();
			scrfilename[2]=Utilities.captureScreenshot(d, "Assign shift details");
			d.findElementById(NAVIAGTIONTOOL_BAR).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		return scrfilename;
	}
	
	//Method for getting text
	public String[] gettingText(AppiumDriver d)
	{
		WebDriverWait wait = new WebDriverWait(d, 60);
		try
		{
			d.findElementById(BTNCANCEL).click();
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.id(GETTINGTXTWISH)));
		String wishtext=d.findElementById(GETTINGTXTWISH).getText();
		System.out.println("Wishes message : "+wishtext);
		String daytext=d.findElementById(GETTINGTXTDAY).getText();
		System.out.println("Day : "+daytext);
		try
		{
			String shifttext=d.findElementById(GETTINGTXTSHIFT).getText();
			System.out.println("Pending shift : "+shifttext);
		}
		catch(Exception e)
		{
			e.getMessage();
			popUp(d);
		}	
		String currentdaytext=d.findElementById(GETTINGTXTCURRENTDAY).getText();
		System.out.println("Current day : "+currentdaytext);
		String schedulecount=d.findElementByXPath(GETTINGTXTSCHEDULE).getText()+" " +d.findElementById(GETTINGTXTSCHEDULECOUNT).getText();
		String shiftoffercount=d.findElementByXPath(GETTINGTXTSHIFTOFFER).getText()+" "+d.findElementById(GETTINGTXTSHIFTOFFERCOUNT).getText();
		System.out.println(schedulecount);
		System.out.println(shiftoffercount);
		return new String[] {wishtext,daytext,currentdaytext,schedulecount,shiftoffercount};
	}
}
