package JST;

public abstract class AbstractWhileLoop extends AbstractStatement
{
	private AbstractExpression _condition;
	private AbstractStatement _operation;
	
	public AbstractWhileLoop(AbstractExpression condition, AbstractStatement operation) {
		_condition = condition;
		_operation = operation;
	}
	
	public AbstractExpression getCondition() {
		return _condition;
	}
	
	public AbstractStatement getOperation() {
		return _operation;
	}
}