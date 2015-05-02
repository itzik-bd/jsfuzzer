package JST;

import JST.Interfaces.Visitor;

public class While extends AbsWhileLoop
{
	public While(AbsExpression condition, StatementsBlock stmtsBlock) {
		super(condition, stmtsBlock);
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}