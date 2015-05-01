package JST;

import JST.Interfaces.Visitor;

public class DoWhile extends AbsWhileLoop
{
	public DoWhile(AbsExpression condition, AbsStatement operation) {
		super(condition, operation);
	}
	
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}