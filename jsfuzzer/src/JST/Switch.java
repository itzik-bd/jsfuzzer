package JST;

import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.Visitor;

public class Switch extends AbsStatement
{
	private AbsExpression _expression;
	private List<CaseBlock> _casesOperations;  
	
	public Switch(AbsExpression expression)
	{
		_expression = expression;
		_casesOperations = new LinkedList<CaseBlock>();
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
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
	
}