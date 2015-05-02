package JST;

import JST.Interfaces.Visitor;

public class This extends AbsExpression
{
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}