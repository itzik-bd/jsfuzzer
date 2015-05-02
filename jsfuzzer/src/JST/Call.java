package JST;

import java.util.List;

import JST.Interfaces.Assignable;
import JST.Interfaces.Visitor;

public class Call extends AbsExpression implements Assignable
{
	
	private AbsExpression _base;
	
	private List<AbsExpression> _params;
	
	public Call(AbsExpression base, List<AbsExpression> params)
	{
		_base = base;
		_params = params;
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
