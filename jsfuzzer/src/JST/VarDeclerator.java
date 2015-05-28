package JST;

import JST.Interfaces.Visitor;

public class VarDeclerator extends JSTNode
{
	private Identifier _id;
	private AbsExpression _init; // can be null
	
	public VarDeclerator(Identifier id, AbsExpression init)
	{
		_id = id;
		_init = init;
	}
	
	public Identifier getId()
	{
		return _id;
	}
	
	public AbsExpression getInit()
	{
		return _init;
	}
	
	public boolean hasInit()
	{
		return (_init != null);
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}