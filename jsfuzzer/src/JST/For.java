package JST;

import JST.Interfaces.Visitor;

public class For extends AbsLoop
{
	private AbsStatement _initStatement;
	private AbsExpression _conditionExpression;
	private AbsExpression _stepExpression;
	
	public For(AbsStatement initStatement, AbsExpression conditionExpression, AbsExpression stepExpression, 
			StatementsBlock stmtsBlock, VarDecleration loopCounterDecl)
	{
		super(stmtsBlock, loopCounterDecl);
		_initStatement = initStatement;
		_conditionExpression = conditionExpression;
		_stepExpression = stepExpression;		
	}
	
	public For(AbsStatement initStatement, AbsExpression conditionExpression, AbsExpression stepExpression, 
			VarDecleration loopCounterDecl)
	{		
		this(initStatement, conditionExpression, stepExpression, new StatementsBlock(), loopCounterDecl);
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
		
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}