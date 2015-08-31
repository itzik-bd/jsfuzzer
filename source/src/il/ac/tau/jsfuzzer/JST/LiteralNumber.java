package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Enums.LiteralTypes;
import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

public class LiteralNumber extends Literal 
{
	// long cannot hold value of JS numbers
	private String _value;
	
	public LiteralNumber(String value) 
	{
		super(LiteralTypes.NUMBER);
		
		_value = value;
	}
	
	public String getValue()
	{
		return _value;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
	
	@Override
	public String toString()
	{
		return _value;
	}
}