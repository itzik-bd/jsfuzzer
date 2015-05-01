package JST;

import JST.Interfaces.Visitor;

public class While extends AbsWhileLoop
{
	public While(AbsExpression condition, AbsStatement operation) {
		super(condition, operation);
	}
	
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}