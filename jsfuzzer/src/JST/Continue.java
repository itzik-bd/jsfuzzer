package JST;

import JST.Interfaces.Visitor;

public class Continue extends AbsStatement
{
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}