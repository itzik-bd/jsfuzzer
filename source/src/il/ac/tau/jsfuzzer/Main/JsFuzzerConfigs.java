package il.ac.tau.jsfuzzer.Main;

import java.util.LinkedList;
import java.util.List;

public class JsFuzzerConfigs
{
	public final static int[] version = {1,0};
	
	public static String getVersion()
	{
		List<String> versionStr = new LinkedList<String>();
		
		for (Integer versionEntity : version) versionStr.add(versionEntity.toString());
		
		return String.join(".", versionStr);
	}
}