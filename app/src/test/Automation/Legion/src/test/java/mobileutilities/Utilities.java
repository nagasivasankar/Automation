package mobileutilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Utilities extends BaseClass {
	// Capture Screen Shot and save in the screen shots folder
	public static String captureScreenshot(WebDriver driver, String screenshotname) {
		String scrfilename=null;
		try {

			// Current Date and Time
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
			Date dt = new Date();
			System.out.println(dateFormat.format(dt));
			scrfilename= screenshotname + "_"+ dateFormat.format(dt) + ".png";

			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(source,
					new File(screenshotFilePath + dateFormat.format(dt) + "_" + screenshotname + ".png"));
			System.out.println("screenshot taken");
		} catch (Exception e) {
			System.out.println("exception while taking screenshot" + e.getMessage());
		}
		return scrfilename;

	}

	// Make zip of reports
	public static void zip(String filepath) {
		try {
			File inFolder = new File(filepath);
			File outFolder = new File("Reports.zip");
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFolder)));
			BufferedInputStream in = null;
			byte[] data = new byte[1000];
			String files[] = inFolder.list();
			for (int i = 0; i < files.length; i++) {
				in = new BufferedInputStream(new FileInputStream(inFolder.getPath() + "/" + files[i]), 1000);
				out.putNextEntry(new ZipEntry(files[i]));
				int count;
				while ((count = in.read(data, 0, 1000)) != -1) {
					out.write(data, 0, count);
				}
				out.closeEntry();
			}
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String date()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date dt = new Date();
		String str=dateFormat.format(dt);
		return str;
	}
}
