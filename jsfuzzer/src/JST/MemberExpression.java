package JST;

public class MemberExpression
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
