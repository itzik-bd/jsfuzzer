package JST;

public class For extends AbstractStatement
{
	private AbstractStatement _initStatement;
	private AbstractExpression _conditionExpression;
	private AbstractExpression _stepExpression;
	private AbstractStatement _operation;
	
	public For(AbstractStatement initStatement, AbstractExpression conditionExpression, AbstractExpression stepExpression, AbstractStatement operation) {
		_initStatement = initStatement;
		_conditionExpression = conditionExpression;
		_stepExpression = stepExpression;		
		_operation = operation;
	}
	
	public AbstractStatement getInitStatement() {
		return _initStatement;
	}
	
	public AbstractStatement getConditionExpression() {
		return _conditionExpression;
	}
	
	public AbstractStatement getStepExpression() {
		return _stepExpression;
	}
	
	public AbstractStatement getOperation() {
		return _operation;
	}
}
