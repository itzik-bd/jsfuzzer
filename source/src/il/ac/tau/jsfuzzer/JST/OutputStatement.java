package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

public class OutputStatement extends AbsStatement 
{
	private AbsExpression _exp;
	
	public OutputStatement(AbsExpression exp)
	{
		_exp = exp;
	}
	
	public AbsExpression getExp()
	{
		return _exp;
	}

	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}
}