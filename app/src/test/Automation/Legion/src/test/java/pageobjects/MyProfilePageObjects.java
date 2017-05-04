package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import mobileutilities.Utilities;

public class MyProfilePageObjects extends mobileutilities.BaseClass {

	// create driver
	public AppiumDriver d;
	// create variable of txtboxfirstname as static final string type
	public static final String TXTBOXFIRSTNAME = "co.legion.client.staging:id/firstName";
	// create variable of txtboxlastname as static final string type
	public static final String TXTBOXLASTNAME = "co.legion.client.staging:id/lastName";
	// create variable of txtboxnickname as static final string type
	public static final String TXTBOXNICKNAME = "co.legion.client.staging:id/nickName";
	// create variable of txtboxmobilenum as static final string type
	public static final String TXTBOXMOBILENUM = "co.legion.client.staging:id/phoneEdittext";
	// create variable of txtboxemailid as static final string type
	public static final String TXTBOXMAILID = "co.legion.client.staging:id/emailEdittext";
	// create variable of txtboxaddress as static final string type
	public static final String TXTBOXADDRESS = "co.legion.client.staging:id/streetEdittext";
	// create variable of txtboxcity as static final string type
	public static final String TXTBOXCITY = "co.legion.client.staging:id/cityEdittext";
	// create variable of selectstate as static final string type
	public static final String SELECTSTATE = "co.legion.client.staging:id/stateSpinner";
	// create variable of selectStatename as static final string type
	public static final String SELECTSTATENAME = "//android.widget.TextView[@text='Alabama']";
	// create variable of txtboxpincode as static final string type
	public static final String TXTBOXPINCODE = "co.legion.client.staging:id/zipcode";
	// create variable of navigationsave as static final String type
	public static final String NAVIGATIONSAVE = "co.legion.client.staging:id/imageviewOk";
	// create variable of btnmore as static final string type
	public static final String BTNMORE = "//android.widget.TextView[@text='More']";
	// create variable of btnprofile as static final string type
	public static final String BTNPROFILE = "co.legion.client.staging:id/profile";
	// create variable of txtboxemailerror as static final string type
	public static final String TXTBOXEMAILERROR = "co.legion.client.staging:id/emailErrorTextProfile";
	// create variable of txtboxphoneerror as static final string type
	public static final String TXTBOXPHONEERROR = "co.legion.client.staging:id/phoneErrorTextProfile";
	// create variable of symbolplus symbol as static final string type
	public static final String SYMBOLPLUS = "co.legion.client.staging:id/uploadImageInvisible";
	// create variable of symbolplus1 symbol as static final string type
	public static final String SYMBOLPLUS1 = "co.legion.client.staging:id/uploadImage";
	// create variable of BTNCANCEL as static final string type
	public static final String BTNCANCEL = "co.legion.client.staging:id/cancelLayout";
	// create variable of BTNCLOSE as static final string type
	public static final String BTNCLOSE = "co.legion.client.staging:id/closeSetup";
	// create variable of BTNDISCARD as static final string type
	public static final String BTNDISCARD = "co.legion.client.staging:id/cancelTV";
	// create variable of BTNCANCEL as static final string type
	public static final String BTNCANCEL1 = "co.legion.client.staging:id/discardTV";
	// create variable of BTNCHOOSEAPHOTO as static final string type
	public static final String BTNCHOOSEAPHOTO = "co.legion.client.staging:id/galleryLayout";
	// create variable of BTNALLOW as static final string type
	public static final String BTNALLOW = "com.android.packageinstaller:id/permission_allow_button";
	// create variable of BTNPHOTO as static final string type
	public static final String BTNPHOTO = "com.google.android.apps.photos:id/title";
	// create variable of SELECTPHOTO as static final string type
	public static final String SELECTPHOTO = "com.google.android.apps.photos:id/title";
	// create variable of SELECTIMAGE as static final string type
	public static final String SELECTIMAGE = "android.view.View[@index='1']";
	// create variable of BTNCANCEL as static final string type
	public static final String BTNCLOSESETUP = "co.legion.client.staging:id/closeSetup";

	// constructor
	public MyProfilePageObjects(AppiumDriver driver) {
		this.d = driver;
	}

	public void myProfile(AppiumDriver d, String firstname, String lastname, String nickname, String mobileno,
			String emailid, String address, String city, String pincode, String cond) {
		WebDriverWait wait = new WebDriverWait(d, 30);
		try {
			d.findElementById(BTNCLOSESETUP).click();
		} catch (Exception e) {
			e.getMessage();
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(BTNMORE)));
		d.findElementByXPath(BTNMORE).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNPROFILE)));
		d.findElementById(BTNPROFILE).click();
		// Utilities.captureScreenshot(d, "Profile");
		wait.until(ExpectedConditions.elementToBeClickable(By.id(TXTBOXFIRSTNAME)));
		try {
			d.findElementById(SYMBOLPLUS1).click();
			Utilities.captureScreenshot(d, "Photo");
			d.findElementById(BTNCANCEL).click();
		} catch (Exception e) {
			e.getMessage();
		}
		// Utilities.captureScreenshot(d, "Photo");
		d.findElementById(TXTBOXFIRSTNAME).clear();
		d.findElementById(TXTBOXFIRSTNAME).sendKeys(firstname);
		// Hiding the keyboard
		key(d);
		d.findElementById(TXTBOXLASTNAME).clear();
		d.findElementById(TXTBOXLASTNAME).sendKeys(lastname);
		// Hiding the keyboard
		key(d);
		d.findElementById(TXTBOXNICKNAME).clear();
		d.findElementById(TXTBOXNICKNAME).sendKeys(nickname);
		// Hiding the keyboard
		key(d);
		d.findElementById(TXTBOXMOBILENUM).clear();
		d.findElementById(TXTBOXMOBILENUM).sendKeys(mobileno);
		// Hiding the keyboard
		key(d);
		d.findElementById(TXTBOXMAILID).clear();
		d.findElementById(TXTBOXMAILID).sendKeys(emailid);
		// Hiding the keyboard
		key(d);
		d.findElementById(TXTBOXADDRESS).clear();
		d.findElementById(TXTBOXADDRESS).sendKeys(address);
		// Hiding the keyboard
		key(d);
		d.findElementById(TXTBOXCITY).clear();
		d.findElementById(TXTBOXCITY).sendKeys(city);
		// Hiding the keyboard
		key(d);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(SELECTSTATE)));
		// Hiding the keyboard
		key(d);
		d.findElementById(SELECTSTATE).click();
		d.scrollTo("Alabama");
		d.findElementByXPath(SELECTSTATENAME).click();
		// Hiding the keyboard
		key(d);
		d.findElementById(TXTBOXPINCODE).clear();
		d.findElementById(TXTBOXPINCODE).sendKeys(pincode);
		// Hiding the keyboard
		key(d);
	}

	// Method for My Profile screen while giving blank and wrong details to the
	// text fields.
	public String[] myProfileError(AppiumDriver d) {
		String str = d.findElementById(TXTBOXPHONEERROR).getText();
		System.out.println("Error message for Mobile: " + str);
		String str1 = d.findElementById(TXTBOXEMAILERROR).getText();
		System.out.println("Error message for Mobile: " + str1);
		return new String[] { str, str1 };
	}

	// Method for click on Save button
	public void save(AppiumDriver d) {
		try {
			d.findElementById(NAVIGATIONSAVE).click();
			WebDriverWait wait=new WebDriverWait(d,60);
			wait.until(ExpectedConditions.elementToBeClickable(By.id(BTNCLOSE)));
			d.findElementById(BTNCLOSE).click();
			d.findElementById(BTNDISCARD).click();	
		} catch (Exception e) {

			e.getMessage();
		}
	}
}
