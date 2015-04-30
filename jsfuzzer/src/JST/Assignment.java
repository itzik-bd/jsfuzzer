package JST;

import JST.Interfaces.Assignable;

public class Assignment extends AbsStatement 
{
	private Assignable _leftHandSide;
	private AbsExpression _expr;
	
	public Assignment(Assignable leftHandSide, AbsExpression expr)
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
