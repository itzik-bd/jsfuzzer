package JST.Helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Generator.Config.ConfigProperties;
import JST.Identifier;
import JST.JSTNode;
import JST.Literal;
import JST.LiteralString;
import JST.RawCode;
import JST.Enums.LiteralTypes;
import Utils.FilesIO;

public class Factory
{
	private Map<String, JSTNode> _constantNodes = new HashMap<String, JSTNode>();
	private Map<LiteralTypes, Literal> _literalNodes = new HashMap<LiteralTypes, Literal>();
	private Map<String, Identifier> _funcIdentifierNodes = new HashMap<String, Identifier>();
	private Map<String, Identifier> _identifierNodes = new HashMap<String, Identifier>();
	private Map<String, Identifier> _loopIdentifierNodes = new HashMap<String, Identifier>();
	private Map<String, RawCode> _snippets = new HashMap<String, RawCode>();
	private Map<String, LiteralString> _objectKeysStrings = new HashMap<String, LiteralString>();

	public Factory()
	{		
		// constant nodes
		_constantNodes.put("break", new JST.Break());
		_constantNodes.put("continue", new JST.Continue());
		_constantNodes.put("this", new JST.This());
		_constantNodes.put("default", new JST.Default());
		_constantNodes.put("lit-function", new JST.LiteralString("function"));
		_constantNodes.put("lit-null", new JST.LiteralString("null"));
		
		// create helper functions name
		_funcIdentifierNodes.put("JSPrint", new Identifier("JSPrint"));
		_funcIdentifierNodes.put("JSCall", new Identifier("JSCall"));
		
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
	
	public RawCode getSnippet(String snippetName)
	{
		if (!_snippets.containsKey(snippetName)) {
			String code = FilesIO.getSnippet(snippetName);
			_snippets.put(snippetName, new RawCode(code));
		}
		
		return _snippets.get(snippetName);
	}
	
	public LiteralString getObjectKeyLiteralString(String key)
	{
		LiteralString literalKey = _objectKeysStrings.get(key);
		
		if(literalKey == null)
		{
			literalKey = new LiteralString(key);
			_objectKeysStrings.put(key, literalKey);
		}
		
		return literalKey;
	}
}