package JST;

import JST.Interfaces.Visitor;

public class DoWhile extends AbsWhileLoop
{
	public DoWhile(AbsExpression condition, StatementsBlock stmtsBlock) {
		super(condition, stmtsBlock);
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}