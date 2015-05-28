package JST.Helper;

import java.util.HashMap;
import java.util.Map;

import JST.Identifier;
import JST.JSTNode;
import JST.Enums.LiteralTypes;

public class Factory
{
	private Map<String, JSTNode> _constantNodes = new HashMap<String, JSTNode>();
	private Map<String, JSTNode> _literalNodes = new HashMap<String, JSTNode>();
	private Map<String, Identifier> _identifierNodes = new HashMap<String, Identifier>();
	
	public Factory()
	{		
		// constant nodes
		_constantNodes.put("break", new JST.Break());
		_constantNodes.put("continue", new JST.Continue());
		_constantNodes.put("this", new JST.This());
		
		// constant literal nodes
		for(LiteralTypes type : LiteralTypes.values())
		{
			if (type.isSingleValue())
			{
				_literalNodes.put(type.getToken(), new JST.Literal(type));
			}
		}
	}
	
	public Identifier getIdentifier(String name)
	{
		Identifier res = _identifierNodes.get(name);
		
		if (res == null)
		{
			res = new Identifier(name);
			_identifierNodes.put(name, res);
		}
		
		return res;
	}
	
	public JSTNode getConstantNode(String name)
	{
		return _constantNodes.get(name);
	}
	
	public JSTNode getSingleLiteralNode(String name)
	{
		return _literalNodes.get(name);
	}
}