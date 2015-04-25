package JST;

public class For extends AbsStatement
{
	private AbsStatement _initStatement;
	private AbsExpression _conditionExpression;
	private AbsExpression _stepExpression;
	private AbsStatement _operation;
	
	public For(AbsStatement initStatement, AbsExpression conditionExpression, AbsExpression stepExpression, AbsStatement operation) {
		_initStatement = initStatement;
		_conditionExpression = conditionExpression;
		_stepExpression = stepExpression;		
		_operation = operation;
	}
	
	public AbsStatement getInitStatement() {
		return _initStatement;
	}
	
	public AbsStatement getConditionExpression() {
		return _conditionExpression;
	}
	
	public AbsStatement getStepExpression() {
		return _stepExpression;
	}
	
	public AbsStatement getOperation() {
		return _operation;
	}
}
