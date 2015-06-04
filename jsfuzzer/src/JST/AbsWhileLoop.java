package JST;

public abstract class AbsWhileLoop extends AbsLoop
{
	private AbsExpression _condition;

	
	public AbsWhileLoop(AbsExpression condition, StatementsBlock stmtsBlock, VarDecleration loopCounterInit)
	{
		super(stmtsBlock, loopCounterInit);
		_condition = condition;
	}
	
	public AbsExpression getCondition()
	{
		return _condition;
	}
}