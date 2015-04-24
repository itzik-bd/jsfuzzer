package JST;

import java.util.List;

public class Switch extends AbstractStatement
{
	private AbstractExpression _expression;
	private List<Case> _cases;
	private Default _defaultCase;
	
	public Switch(AbstractExpression expression, List<Case> cases, Default defaultCase)
	{
		_expression = expression;
		_cases = cases;
		_defaultCase = defaultCase;
	}

	public Switch(AbstractExpression expression, List<Case> cases)
	{
		this(expression, cases, null);	
	}
	
	public AbstractExpression getExpression() 
	{
		return _expression;
	}
	
	public List<Case> getCases() 
	{
		return _cases;
	}
	
	public Default getDefaultCase()
	{
		return _defaultCase;
	}
	
	public boolean hasDefaultCase()
	{
		return (_defaultCase != null);
	}
}