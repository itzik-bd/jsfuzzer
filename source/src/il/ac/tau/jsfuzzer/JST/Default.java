package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Caseable;
import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

public class Default extends JSTNode implements Caseable 
{

	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}

}
