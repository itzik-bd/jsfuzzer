package JST;

public class Case extends AbstractStatement
{
	private AbstractExpression _expression;
	
	public Case(AbstractExpression expression) {
		_expression = expression;
	}
	
	public AbstractExpression getExpression() {
		return _expression;
	}
}
