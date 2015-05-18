package Generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GeneratorUtils
{
	private static final String DEFAULT_CONFIG_FILE = "resources/config/config.properties";
	
	public static Properties loadConfigFile(String propertiesFile) throws IOException
	{		
		String actualFile = (propertiesFile != null) ? propertiesFile : DEFAULT_CONFIG_FILE;
		
		Properties p = new Properties();
		
		InputStream input = new FileInputStream(actualFile);
		p.load(input);
		input.close();
		
		return p;
	}
}