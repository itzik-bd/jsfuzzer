package JST;

public class If extends AbstractStatement
{
	private AbstractExpression _condition;
	private AbstractStatement _operation;
	private AbstractStatement _elseOperation;
	
	public If(AbstractExpression condition, AbstractStatement operation, AbstractStatement elseOperation) {
		_condition = condition;
		_operation = operation;
		_elseOperation = elseOperation;
	}
	
	public If(AbstractExpression condition, AbstractStatement operation) {
		_condition = condition;
		_operation = operation;
		_elseOperation = null;
	}
	
	public AbstractExpression getCondition() {
		return _condition;
	}
	
	public AbstractStatement getOperation() {
		return _operation;
	}
	
	public AbstractStatement getElseOperation() {
		return _elseOperation;
	}
	
	public boolean hasElse() {
		return (_elseOperation == null);
	}
}
