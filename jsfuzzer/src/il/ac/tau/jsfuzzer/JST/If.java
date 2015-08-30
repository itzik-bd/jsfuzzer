package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

public class If extends AbsStatement
{
	private AbsExpression _condition;
	private StatementsBlock _stmtsBlock;
	private StatementsBlock _elseStmtsBlock;
	
	public If(AbsExpression condition, StatementsBlock stmtsBlock, StatementsBlock elseStmtsBlock) {
		_condition = condition;
		_stmtsBlock = stmtsBlock;
		_elseStmtsBlock = elseStmtsBlock;
	}
	
	public AbsExpression getCondition() {
		return _condition;
	}
	
	public AbsStatement getStatementsBlock() {
		return _stmtsBlock;
	}
	
	public AbsStatement getElseStatementsBlock() {
		return _elseStmtsBlock;
	}
	
	public boolean hasElse() {
		return (_elseStmtsBlock != null);
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}