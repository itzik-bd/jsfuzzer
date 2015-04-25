package JST;

import java.util.LinkedList;
import java.util.List;

import ThirdParty.Pair;

public class Switch extends AbsStatement
{
	private AbsExpression _expression;
	private List<Pair<List<AbsExpression>, AbsStatement>> _casesOperations;  
	
	public Switch(AbsExpression expression, List<Pair<List<AbsExpression>, AbsStatement>> casesOps)
	{
		_expression = expression;
		_casesOperations = casesOps;
	}

	public Switch(AbsExpression expression)
	{
		this(expression, new LinkedList<Pair<List<AbsExpression>, AbsStatement>>());	
	}
	
	public AbsExpression getExpression() 
	{
		return _expression;
	}

	public List<Pair<List<AbsExpression>, AbsStatement>> getCasesOps()
	{
		return _casesOperations;
	}
	
	public void addCaseOp(List<AbsExpression> cases, AbsStatement stmt)
	{
		_casesOperations.add(new Pair<List<AbsExpression>, AbsStatement>(cases, stmt));
	}
}