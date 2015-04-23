package JST;

public class For extends AbstractStatement
{
	private AbstractStatement _initStatement;
	private AbstractExpression _conditionExpression;
	private AbstractExpression _stepExpression;
	
	public For(AbstractStatement initStatement, AbstractExpression conditionExpression, AbstractExpression stepExpression) {
		_initStatement = initStatement;
		_conditionExpression = conditionExpression;
		_stepExpression = stepExpression;		
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
}
