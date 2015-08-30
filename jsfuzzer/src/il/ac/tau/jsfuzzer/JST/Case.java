package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Caseable;
import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

public class Case extends JSTNode implements Caseable
{
	private AbsExpression _caseExpr;
	
	public Case(AbsExpression caseExpr)
	{
		_caseExpr = caseExpr;
	}
	
	public AbsExpression getCaseExpr()
	{
		return _caseExpr;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}
	
	

}
