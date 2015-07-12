package JST;

import java.util.Arrays;
import java.util.List;

import JST.Interfaces.Visitor;

public class Call extends AbsExpression
{
	private AbsExpression _base;
	
	private List<AbsExpression> _params;
	
	public Call(AbsExpression base, List<AbsExpression> params)
	{
		_base = base;
		_params = params;
	}
	
	public Call(AbsExpression base, AbsExpression ... params)
	{
		this(base, Arrays.asList(params));
	}
	
	public AbsExpression getBase()
	{
		return _base;
	}
	
	public List<AbsExpression> getParams()
	{
		return _params;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}
