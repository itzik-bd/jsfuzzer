package JST;

import JST.Enums.LiteralTypes;
import JST.Interfaces.Visitor;

public class LiteralString extends Literal 
{
	String _value;
	
	public LiteralString(String value) 
	{
		super(LiteralTypes.STRING);
		
		_value = value;
	}
	
	public String getValue()
	{
		return _value;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}