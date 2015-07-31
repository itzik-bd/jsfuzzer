package Generator.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import Utils.FilesIO;

public class Configs
{
	private static final String DEFAULT_CONFIG_FILE = "resources/config/config.properties";
	
	private Map<ConfigProperties, Object> _configsMap = new HashMap<ConfigProperties, Object>();
	
	public static Configs loadConfigFile(String configFile) throws Exception
	{
		// load properties
		String actualFile = (configFile != null) ? configFile : DEFAULT_CONFIG_FILE;
		Properties p = FilesIO.loadPropertiesFile(actualFile);
		
		StringBuffer errorsList = new StringBuffer();
		boolean errorFound = false;
		
		Configs res = new Configs();
		
		for(ConfigProperties prop : ConfigProperties.values())
		{
			String valStr = null;
			Object valParsed = null;
			
			// get properties
			 valStr = p.getProperty(prop.toString());
			 if(valStr == null)
			 {
				 errorsList.append(String.format("config property %s doesn't exists\n", prop));
				 errorFound = true;
			 }
			 else
			 {
				// validate types
				try {
					valParsed = parseValue(valStr, prop);
				} catch (Exception e) {
					errorsList.append(String.format("config property %s is not from type %s\n", prop, prop.getClassType().getName()));
					errorFound = true;
				}
				
				// add property to configs map
				res._configsMap.put(prop, valParsed);
			 }
		}
		
		// check if errors occurred
		if (errorFound)
		{
			throw new Exception(errorsList.toString());
		}
		
		return res;
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
		else if (prop.getClassType() == String.class)
		{
			return valStr;
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
	
	public String valString(ConfigProperties prop)
	{
		return (String) _configsMap.get(prop);
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