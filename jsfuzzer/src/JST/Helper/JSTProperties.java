package JST.Helper;

import java.util.Arrays;
import java.util.List;

import JST.Enums.JSTNodes;

/** This class is meant to remember all JST nodes that implements each interface */

public class JSTProperties
{
	private List<JSTNodes> objectKeys;
	private List<JSTNodes> assignable;
	
	public JSTProperties()
	{
		objectKeys = Arrays.asList(JSTNodes.Identifier, JSTNodes.LiteralString);
		assignable = Arrays.asList(JSTNodes.Identifier); // TODO: add "MemberExp" to list when this jst will be supported!!!!!!!
	}

	public List<JSTNodes> getObjectKeys()
	{
		return objectKeys;
	}

	public List<JSTNodes> getAssignable() {
		return assignable;
	}
}
