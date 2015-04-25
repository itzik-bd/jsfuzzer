package JST;

public class If extends AbsStatement
{
	private AbsExpression _condition;
	private AbsStatement _operation;
	private AbsStatement _elseOperation;
	
	public If(AbsExpression condition, AbsStatement operation, AbsStatement elseOperation) {
		_condition = condition;
		_operation = operation;
		_elseOperation = elseOperation;
	}
	
	public If(AbsExpression condition, AbsStatement operation) {
		this(condition, operation, null);
	}
	
	public AbsExpression getCondition() {
		return _condition;
	}
	
	public AbsStatement getOperation() {
		return _operation;
	}
	
	public AbsStatement getElseOperation() {
		return _elseOperation;
	}
	
	public boolean hasElse() {
		return (_elseOperation != null);
	}
}
