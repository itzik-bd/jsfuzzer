package JST;

import JST.Interfaces.Assignable;

public class CompoundAssignment extends AbsStatement 
{
	private Assignable _leftHandSide;
	private AbsExpression _expr;
	
	public CompoundAssignment(Assignable leftHandSide, AbsExpression expr)
	{
		_leftHandSide = leftHandSide;
		_expr = expr;
	}
	
	public Assignable getLeftHandSide()
	{
		return _leftHandSide;
	}
	
	public AbsExpression getExpr()
	{
		return _expr;
	}
}