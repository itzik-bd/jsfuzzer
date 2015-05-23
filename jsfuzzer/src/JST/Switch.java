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
	
	public void addCaseOp(CaseBlock caseBlock)
	{
		_casesOperations.add(caseBlock);
	}
	
	public void addCaseOp(List<AbsExpression> cases, List<AbsStatement> stmt)
	{
		_casesOperations.add(new CaseBlock(cases, stmt));
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
	
}