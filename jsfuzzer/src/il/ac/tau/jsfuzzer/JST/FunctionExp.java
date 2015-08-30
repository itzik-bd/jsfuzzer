 package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

import java.util.LinkedList;
import java.util.List;

public class FunctionExp extends AbsExpression
{
	private final List<Identifier> _formals;
	private final StatementsBlock _stmtsBlock;
	
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