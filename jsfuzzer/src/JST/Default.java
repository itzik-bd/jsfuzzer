package JST;

import JST.Interfaces.Caseable;
import JST.Interfaces.Visitor;

public class Default extends JSTNode implements Caseable 
{

	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}

}
