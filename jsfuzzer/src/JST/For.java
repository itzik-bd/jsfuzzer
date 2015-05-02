package JST;

import JST.Interfaces.Visitor;

public class For extends AbsStatement
{
	private AbsStatement _initStatement;
	private AbsExpression _conditionExpression;
	private AbsExpression _stepExpression;
	private StatementsBlock _stmtsBlock;
	
	public For(AbsStatement initStatement, AbsExpression conditionExpression, AbsExpression stepExpression, StatementsBlock stmtsBlock) {
		_initStatement = initStatement;
		_conditionExpression = conditionExpression;
		_stepExpression = stepExpression;		
		_stmtsBlock = stmtsBlock;
	}
	
	public AbsStatement getInitStatement() {
		return _initStatement;
	}
	
	public AbsStatement getConditionExpression() {
		return _conditionExpression;
	}
	
	public AbsStatement getStepExpression() {
		return _stepExpression;
	}
	
	public AbsStatement getStatementsBlock() {
		return _stmtsBlock;
	}
	
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}