package JST;

import JST.Interfaces.Visitor;

public class This extends AbsExpression
{
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}