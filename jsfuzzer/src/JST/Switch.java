package JST;

import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.Visitor;

public class Switch extends AbsStatement
{
	private AbsExpression _expression;
	private List<CaseBlock> _casesOperations;  
	
	public Switch(AbsExpression expression, List<CaseBlock> casesOps)
	{
		_expression = expression;
		_casesOperations = casesOps;
	}

	public Switch(AbsExpression expression)
	{
		this(expression, new LinkedList<CaseBlock>());	
	}
	
	public AbsExpression getExpression() 
	{
		return _expression;
	}

	public List<CaseBlock> getCasesOps()
	{
		return _casesOperations;
	}
	
	public void addCaseOp(List<AbsExpression> cases, List<AbsStatement> stmt)
	{
		_casesOperations.add(new CaseBlock(cases, stmt));
	}
	
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
	
	public class CaseBlock extends JSTNode
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
		
		@Override
		public Object accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}
}