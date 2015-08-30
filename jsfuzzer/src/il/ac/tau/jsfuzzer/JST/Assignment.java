package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Assignable;
import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

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
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}
