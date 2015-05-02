package JST;

public abstract class AbsWhileLoop extends AbsStatement
{
	private AbsExpression _condition;
	private StatementsBlock _stmtsBlock;
	
	public AbsWhileLoop(AbsExpression condition, StatementsBlock stmtsBlock) {
		_condition = condition;
		_stmtsBlock = stmtsBlock;
	}
	
	public AbsExpression getCondition() {
		return _condition;
	}
	
	public StatementsBlock getStatementsBlock() {
		return _stmtsBlock;
	}
}