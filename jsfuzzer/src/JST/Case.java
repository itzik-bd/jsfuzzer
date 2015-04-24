package JST;

public class Case extends AbstractStatement
{
	private AbstractExpression _expression;
	private AbstractStatement _operation;
	
	public Case(AbstractExpression expression, AbstractStatement operation)
	{
		_expression = expression;
		_operation = operation;
	}
	
	public AbstractExpression getExpression() 
	{
		return _expression;
	}
	
	public AbstractStatement getOperation()
	{
		return _operation;
	}
}
