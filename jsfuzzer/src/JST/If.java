package JST;

import JST.Interfaces.Visitor;

public class If extends AbsStatement
{
	private AbsExpression _condition;
	private StatementsBlock _stmtsBlock;
	private AbsStatement _elseOperation;
	
	public If(AbsExpression condition, StatementsBlock stmtsBlock, AbsStatement elseOperation) {
		_condition = condition;
		_stmtsBlock = stmtsBlock;
		_elseOperation = elseOperation;
	}
	
	public If(AbsExpression condition, StatementsBlock stmtsBlock) {
		this(condition, stmtsBlock, null);
	}
	
	public AbsExpression getCondition() {
		return _condition;
	}
	
	public AbsStatement getStatementsBlock() {
		return _stmtsBlock;
	}
	
	public AbsStatement getElseOperation() {
		return _elseOperation;
	}
	
	public boolean hasElse() {
		return (_elseOperation != null);
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}