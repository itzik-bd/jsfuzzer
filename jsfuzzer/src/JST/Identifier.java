package JST;

import JST.Interfaces.Assignable;
import JST.Interfaces.ObjectKeys;
import JST.Interfaces.Visitor;

public class Identifier extends AbsExpression implements ObjectKeys, Assignable
{
	private String _name;
	
	public Identifier(String name)
	{
		_name = name;
	}
	
	public String getName()
	{
		return _name;
	}
	
	@Override
	public int hashCode()
	{
		return _name.hashCode();	
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other == this)
			return true;
		if(other == null)
			return false;
		if(!(other instanceof Identifier))
			return false;
		
		return _name.equals(((Identifier) other).getName());
	}
	
	@Override
	public String toString()
	{
		return _name;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}