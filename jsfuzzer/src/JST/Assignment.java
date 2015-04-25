package JST;

public class Assignment extends AbsStatement 
{
	private Identifier _id;
	private AbsExpression _expr;
	
	public Assignment(Identifier id, AbsExpression expr)
	{
		_id = id;
		_expr = expr;
	}
	
	public Identifier getId()
	{
		return _id;
	}
	
	public AbsExpression getExpr()
	{
		return _expr;
	}
}
