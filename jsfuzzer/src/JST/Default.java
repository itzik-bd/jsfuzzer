package JST;

public class Default extends AbstractStatement
{
	private AbstractStatement _operation;
	
	public Default(AbstractStatement operaration)
	{
		_operation = operaration;
	}
	
	public AbstractStatement getOperation()
	{
		return _operation;
	}
}
