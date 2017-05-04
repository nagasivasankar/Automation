package mobileutilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigFileUtility {
	public File src;
	public FileInputStream fis;
	public static Properties prop;

	// Method for get the config property file
	public void loadPropertyFile() throws Exception {
		src = new File("./ConfigSetUp/ConfigFile.properties");
		fis = new FileInputStream(src);
		prop = new Properties();
		prop.load(fis);
	}

	// Method for Getting the values from config property file
	public String getProperty(String propKey) {
		return prop.getProperty(propKey);
	}
}
