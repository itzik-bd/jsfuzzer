package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Assignable;
import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

/*
a['aaa']
a.aa
a[3]
a[3+5]
*/
public class MemberExp extends AbsExpression implements Assignable
{
	private AbsExpression _base;
	private AbsExpression _location;
	
	public MemberExp(AbsExpression base, AbsExpression location)
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
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}