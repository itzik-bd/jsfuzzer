package JST;

import JST.Enums.LiteralTypes;
import JST.Interfaces.Visitor;

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
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}