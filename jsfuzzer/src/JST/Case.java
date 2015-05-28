package JST;

import JST.Interfaces.Caseable;
import JST.Interfaces.Visitor;

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
