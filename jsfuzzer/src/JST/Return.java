package JST;

import JST.Interfaces.Visitor;

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
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}
