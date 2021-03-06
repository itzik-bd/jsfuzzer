package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Enums.LiteralTypes;
import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

public class Literal extends AbsExpression 
{
	private LiteralTypes _type;
	
	public Literal(LiteralTypes type)
	{
		_type = type;
	}
	
	public LiteralTypes getType()
	{
		return _type; 
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}