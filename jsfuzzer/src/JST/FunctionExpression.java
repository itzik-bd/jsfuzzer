 package JST;

import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.Visitor;

public class FunctionExpression extends AbsExpression
{
	private List<Identifier> _formals;
	private List<AbsStatement> _statements;
	
	public FunctionExpression(List<Identifier> formals)
	{
		_formals = formals;
		_statements = new LinkedList<AbsStatement>();
	}
	
	public FunctionExpression()
	{
		this(new LinkedList<Identifier>());
	}
	
	public List<Identifier> getFormals()
	{
		return _formals;
	}
	
	public boolean hasFormals()
	{
		return (_formals != null);
	}
	
	public List<AbsStatement> getStatements()
	{
		return _statements;
	}
	
	public void addStatement(AbsStatement stmt)
	{
		_statements.add(stmt);
	}
	
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}