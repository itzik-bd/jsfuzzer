package JST;

import JST.Interfaces.Visitor;

public class While extends AbsWhileLoop
{
	public While(AbsExpression condition, StatementsBlock stmtsBlock, VarDecleration loopCounterDecleration)
	{
		super(condition, stmtsBlock, loopCounterDecleration);
	}
	
	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}
}