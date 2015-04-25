package JST;

public class Return extends AbsStatement
{
	private AbsExpression _returnedValue;
	
	public Return()
	{
		_returnedValue = null;
	}
	
	public Return(AbsExpression value)
	{
		_returnedValue = value;
	}
	
	public boolean hasValue()
	{
		return (_returnedValue != null);
	}
}
