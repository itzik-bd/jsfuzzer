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
	
	public class CaseBlock
	{
		private List<AbsExpression> _cases;
		
		private List<AbsStatement> _statements;
		
		public CaseBlock(List<AbsExpression> cases, List<AbsStatement> statements)
		{
			_cases = cases;
			_statements = statements;
		}
		
		public CaseBlock(List<AbsExpression> cases)
		{
			_cases = cases;
			_statements = new LinkedList<AbsStatement>();
		}
		
		public void addStatement(AbsStatement stmt)
		{
			_statements.add(stmt);
		}
		
		public List<AbsExpression> getCases()
		{
			return _cases;
		}
		
		public List<AbsStatement> getStatements()
		{
			return _statements;
		}	
	}
}