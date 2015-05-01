package JST;

import JST.Interfaces.Assignable;
import JST.Interfaces.ObjectKeys;

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
}