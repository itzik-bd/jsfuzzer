package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Assignable;
import il.ac.tau.jsfuzzer.JST.Interfaces.ObjectKeys;
import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

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
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof Identifier))
			return false;
		
		return _name.equals(((Identifier) obj).getName());
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