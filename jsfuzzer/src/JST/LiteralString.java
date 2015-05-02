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
	
	@Override
    public int hashCode()
	{
        return _value.hashCode();
    }
	
	@Override
	public boolean equals(Object other)
	{
		if (other == this)
			return true;
		if(other == null)
			return false;
		if(!(other instanceof LiteralString))
			return false;
		
		return _value.equals(((LiteralString) other).getValue());
	}
}