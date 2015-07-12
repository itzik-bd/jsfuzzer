package JST.Helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import JST.Identifier;
import JST.JSTNode;
import JST.Literal;
import JST.Enums.LiteralTypes;

public class Factory
{
	private Map<String, JSTNode> _constantNodes = new HashMap<String, JSTNode>();
	private Map<LiteralTypes, Literal> _literalNodes = new HashMap<LiteralTypes, Literal>();
	private Map<String, Identifier> _funcIdentifierNodes = new HashMap<String, Identifier>();
	private Map<String, Identifier> _identifierNodes = new HashMap<String, Identifier>();
	private Map<String, Identifier> _loopIdentifierNodes = new HashMap<String, Identifier>();
	private Identifier _JSPrintFunction;

	public Factory()
	{		
		// constant nodes
		_constantNodes.put("break", new JST.Break());
		_constantNodes.put("continue", new JST.Continue());
		_constantNodes.put("this", new JST.This());
		_constantNodes.put("default", new JST.Default());
		_constantNodes.put("lit-function", new JST.LiteralString("function"));
		_constantNodes.put("lit-null", new JST.LiteralString("null"));
		
		// constant literal nodes
		for(LiteralTypes type : LiteralTypes.values())
		{
			if (type.isSingleValue())
			{
				_literalNodes.put(type, new JST.Literal(type));
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

	public Identifier getLoopIdentifier(String name)
	{
		Identifier res = _loopIdentifierNodes.get(name);
		
		if (res == null)
		{
			res = new Identifier(name);
			_loopIdentifierNodes.put(name, res);
		}
		
		return res;
	}
	
	public Identifier getFuncIdentifier(String name)
	{
		Identifier res = _funcIdentifierNodes.get(name);
		
		if (res == null)
		{
			res = new Identifier(name);
			_funcIdentifierNodes.put(name, res);
		}
		
		return res;
	}
	
	public JSTNode getConstantNode(String name)
	{
		return _constantNodes.get(name);
	}
	
	public Literal getSingleLiteralNode(LiteralTypes type)
	{
		return _literalNodes.get(type);
	}
	
	public List<Identifier> getAllVars()
	{
		return new LinkedList<Identifier>(_identifierNodes.values());
	}
	
	public List<Identifier> getAllLoopVar()
	{
		return new LinkedList<Identifier>(_loopIdentifierNodes.values());
	}
	
	public Identifier getJSPrintID()
	{
		return _JSPrintFunction; 
	}
	
	public void setJSPrintID(Identifier id)
	{
		_JSPrintFunction = id;
	}
}