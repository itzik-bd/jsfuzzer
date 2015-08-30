package il.ac.tau.jsfuzzer.JST.Helper;

import il.ac.tau.jsfuzzer.JST.Identifier;
import il.ac.tau.jsfuzzer.JST.JSTNode;
import il.ac.tau.jsfuzzer.JST.Literal;
import il.ac.tau.jsfuzzer.JST.LiteralString;
import il.ac.tau.jsfuzzer.JST.RawCode;
import il.ac.tau.jsfuzzer.JST.Enums.LiteralTypes;
import il.ac.tau.jsfuzzer.Utils.FilesIO;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		_constantNodes.put("break", new il.ac.tau.jsfuzzer.JST.Break());
		_constantNodes.put("continue", new il.ac.tau.jsfuzzer.JST.Continue());
		_constantNodes.put("this", new il.ac.tau.jsfuzzer.JST.This());
		_constantNodes.put("default", new il.ac.tau.jsfuzzer.JST.Default());
		_constantNodes.put("lit-function", new il.ac.tau.jsfuzzer.JST.LiteralString("function"));
		_constantNodes.put("lit-null", new il.ac.tau.jsfuzzer.JST.LiteralString("null"));
		
		// create helper functions name
		_funcIdentifierNodes.put("JSPrint", new Identifier("JSPrint"));
		_funcIdentifierNodes.put("JSCall", new Identifier("JSCall"));
		
		// constant literal nodes
		for(LiteralTypes type : LiteralTypes.values())
		{
			if (type.isSingleValue())
			{
				_literalNodes.put(type, new il.ac.tau.jsfuzzer.JST.Literal(type));
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