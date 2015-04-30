package JST;

import java.util.List;

import JST.Interfaces.Assignable;

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
}
