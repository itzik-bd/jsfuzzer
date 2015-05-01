package JST;

import JST.Enums.literalTypes.*;

public class LitNumber extends Literal 
{
	// long cannot hold value of JS numbers
	private String _value;
	
	public LitNumber(String value) 
	{
		super(litTypes.LIT_NUMBER);
		
		_value = value;
	}
	
	public String getValue()
	{
		return _value;
	}
}
