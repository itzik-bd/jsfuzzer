package JST;

public abstract class AbsWhileLoop extends AbsStatement
{
	private AbsExpression _condition;
	private AbsStatement _operation;
	
	public AbsWhileLoop(AbsExpression condition, AbsStatement operation) {
		_condition = condition;
		_operation = operation;
	}
	
	public AbsExpression getCondition() {
		return _condition;
	}
	
	public AbsStatement getOperation() {
		return _operation;
	}
}