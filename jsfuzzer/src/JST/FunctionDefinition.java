package JST;

import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.Visitor;

public class FunctionDefinition extends AbsStatement
{
	private Identifier _id;
	private List<Identifier> _formals;
	private StatementsBlock _stmtsBlock;
	
	public FunctionDefinition(Identifier id, List<Identifier> formals)
	{
		_id = id;
		_formals = formals;
		_stmtsBlock = new StatementsBlock();
	}
	
	public FunctionDefinition(Identifier id)
	{
		this(id, new LinkedList<Identifier>());
	}
	
	public Identifier getId()
	{
		return _id;
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
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}
