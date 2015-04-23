package JST;

import java.util.List;

public class Switch extends AbstractStatement
{
	private AbstractExpression _expression;
	private List<AbstractStatement> _statements;
	
	public Switch(AbstractExpression expression, List<AbstractStatement> statements) {
		_expression = expression;
		_statements = statements;
	}
	
	public AbstractExpression getExpression() {
		return _expression;
	}
	
	public List<AbstractStatement> getStatements() {
		return _statements;
	}
}