package JST;

import JST.Interfaces.Visitor;

public class DoWhile extends AbsWhileLoop
{
	public DoWhile(AbsExpression condition, StatementsBlock stmtsBlock, VarDecleration loopCounterDecleration)
	{
		super(condition, stmtsBlock, loopCounterDecleration);
	}
	
	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}
}