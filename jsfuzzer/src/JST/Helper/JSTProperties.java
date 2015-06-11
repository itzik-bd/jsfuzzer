package JST.Helper;

import java.util.Arrays;
import java.util.List;

/** This class is meant to remember all JST nodes that implements each interface */

public class JSTProperties
{
	private List<String> objectKeys;
	private List<String> assignable;
	
	public JSTProperties()
	{
		objectKeys = Arrays.asList("Identifier", "LiteralString");
		assignable = Arrays.asList("Identifier"); // TODO: add "MemberExp" to list when this jst will be supported!!!!!!!
	}

	public List<String> getObjectKeys()
	{
		return objectKeys;
	}

	public List<String> getAssignable() {
		return assignable;
	}
}
