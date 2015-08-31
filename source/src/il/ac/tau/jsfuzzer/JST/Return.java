package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

public class Return extends AbsStatement
{
	private AbsExpression _returnedValue;
	
	public Return()
	{
		_returnedValue = null;
	}
	
	public Return(AbsExpression value)
	{
		_returnedValue = value;
	}
	
	public boolean hasValue()
	{
		return (_returnedValue != null);
	}
	
	public AbsExpression getValue()
	{
		return _returnedValue;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}
