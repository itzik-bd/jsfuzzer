 package JST;

import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.Visitor;

public class FunctionExp extends AbsExpression
{
	private List<Identifier> _formals;
	private StatementsBlock _stmtsBlock;
	
	public FunctionExp(List<Identifier> formals, StatementsBlock stmts)
	{
		_formals = formals;
		_stmtsBlock = stmts;
	}
	
	public FunctionExp(List<Identifier> formals)
	{
		_formals = formals;
		_stmtsBlock = new StatementsBlock();
	}
	
	public FunctionExp()
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
	
	public StatementsBlock getStatementsBlock()
	{
		return _stmtsBlock;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}