package JST;

import JST.Interfaces.Visitor;

public class Break extends AbsStatement
{
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}