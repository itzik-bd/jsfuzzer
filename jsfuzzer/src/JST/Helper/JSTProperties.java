package JST.Helper;

import java.util.Arrays;
import java.util.List;

/** This class is meant to remember all JST nodes that implements each interface */

public class JSTProperties
{
	private List<String> objectKeys;
	
	public JSTProperties()
	{
		objectKeys = Arrays.asList("Identifier", "LiteralString");
	}

	public List<String> getObjectKeys()
	{
		return objectKeys;
	}


}
