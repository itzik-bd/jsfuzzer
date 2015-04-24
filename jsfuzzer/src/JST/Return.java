package JST;

public class Return extends AbstractStatement
{
	private AbstractExpression _returnedValue;
	
	public Return()
	{
		_returnedValue = null;
	}
	
	public Return(AbstractExpression value)
	{
		_returnedValue = value;
	}
	
	public boolean hasValue()
	{
		return (_returnedValue != null);
	}
}
