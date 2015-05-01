package JST;

import JST.Enums.literalTypes.*;

public class Literal extends AbsExpression 
{
	private litTypes _type;
	
	public Literal(litTypes type)
	{
		_type = type;
	}
	
	public litTypes getType()
	{
		return _type; 
	}
}
