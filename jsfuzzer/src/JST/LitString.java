package JST;

import JST.Enums.literalTypes.*;

public class LitString extends Literal 
{
	String _value;
	
	public LitString(String value) 
	{
		super(litTypes.LIT_STRING);
		
		_value = value;
	}
	
	public String getValue()
	{
		return _value;
	}
}
