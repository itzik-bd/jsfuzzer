package JST;

import JST.Interfaces.Assignable;

/*
a['aaa']
a.aa
a[3]
a[3+5]
*/
public class MemberExpression implements Assignable
{
	private AbsExpression _base;
	private AbsExpression _location;
	
	public MemberExpression(AbsExpression base, AbsExpression location)
	{
		_base = base;
		_location = location;
	}
	
	public AbsExpression getBase()
	{
		return _base;
	}
	
	public AbsExpression getLocation()
	{
		return _location;
	}
}
