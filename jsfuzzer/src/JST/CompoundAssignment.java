package JST;

import JST.Enums.CompoundOps;
import JST.Interfaces.Assignable;
import JST.Interfaces.Visitor;

public class CompoundAssignment extends AbsStatement 
{
	private Assignable _leftHandSide;
	private CompoundOps _op;
	private AbsExpression _expr;
	
	public CompoundAssignment(Assignable leftHandSide, CompoundOps op, AbsExpression expr)
	{
		_leftHandSide = leftHandSide;
		_op = op;
		_expr = expr;
	}
	
	public Assignable getLeftHandSide()
	{
		return _leftHandSide;
	}
	
	public CompoundOps getCompoundOp()
	{
		return _op;
	}
	
	public AbsExpression getExpr()
	{
		return _expr;
	}
	
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}