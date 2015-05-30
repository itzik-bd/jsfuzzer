package Generator.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configs
{
	private static final String DEFAULT_CONFIG_FILE = "resources/config/config.properties";
	
	private Map<ConfigProperties, Object> _configsMap = new HashMap<ConfigProperties, Object>();
	
	public static Configs loadConfigFile(String configFile) throws IOException
	{
		String actualFile = (configFile != null) ? configFile : DEFAULT_CONFIG_FILE;
		
		Configs res = new Configs();
		boolean errorFound = false;
		Properties p = new Properties();
		
		InputStream input = new FileInputStream(actualFile);
		p.load(input);
		input.close();
		
		for(ConfigProperties prop : ConfigProperties.values())
		{
			String valStr = null;
			Object valParsed = null;
			
			// get properties
			 valStr = p.getProperty(prop.toString());
			 if(valStr == null)
			 {
				System.err.println(String.format("config property %s doesn't exists", prop));
				errorFound = true;
			 }
			 else
			 {
				// validate types
				try {
					valParsed = parseValue(valStr, prop);
				} catch (Exception e) {
					System.err.println(String.format("config property %s is not from type %s", prop, prop.getClassType().getName()));
					errorFound = true;
				}
				
				// add property to configs map
				res._configsMap.put(prop, valParsed);
			 }
		}
		
		// return null if error occurred
		return errorFound ? null : res;
	}
	
	private static Object parseValue(String valStr, ConfigProperties prop) throws Exception
	{
		if (prop.getClassType() == Integer.class)
		{
			return Integer.parseInt(valStr);
		}
		else if (prop.getClassType() == Double.class)
		{
			return Double.parseDouble(valStr);
		}
		
		throw new Exception(String.format("error in enum ConfigProperties: found property %s with unkown type", prop));
	}
	
	public int valInt(ConfigProperties prop)
	{
		return (int) _configsMap.get(prop);
	}
	
	public double valDouble(ConfigProperties prop)
	{
		return (double) _configsMap.get(prop);
	}
	
	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		
		s.append("Configuration used:\n\n");
		
		for (Map.Entry<ConfigProperties, Object> pair : _configsMap.entrySet())
		{
			s.append(pair.getKey() + "=" + pair.getValue() + "\n");
		}
		
		return s.toString();
	}
}