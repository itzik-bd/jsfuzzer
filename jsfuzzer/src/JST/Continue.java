package JST;

import JST.Interfaces.Visitor;

public class Continue extends AbsStatement
{
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}