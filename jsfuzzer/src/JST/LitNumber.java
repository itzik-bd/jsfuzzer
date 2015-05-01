package JST;

public class LitNumber extends Literal 
{
	// long cannot hold value of JS numbers
	private String _value;
	
	public LitNumber(String value) 
	{
		super(literalType.LIT_NUMBER);
		
		_value = value;
	}
	
	public String getValue()
	{
		return _value;
	}
}
