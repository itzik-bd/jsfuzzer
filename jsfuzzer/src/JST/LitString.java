package JST;

public class LitString extends Literal 
{
	String _value;
	
	public LitString(String value) 
	{
		super(literalType.LIT_STRING);
		
		_value = value;
	}
	
	public String getValue()
	{
		return _value;
	}
}
