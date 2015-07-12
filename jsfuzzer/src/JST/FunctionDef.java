package JST;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.Visitor;

public class FunctionDef extends AbsStatement
{
	private Identifier _id;
	private List<Identifier> _formals;
	private StatementsBlock _stmtsBlock;
	
	public FunctionDef(Identifier id, List<Identifier> formals, StatementsBlock stmtsBlock)
	{
		_id = id;
		_formals = formals;
		_stmtsBlock = stmtsBlock;
	}
	
	public FunctionDef(Identifier id, Identifier ... formals)
	{
		this(id, Arrays.asList(formals), new StatementsBlock());
	}
	
	public FunctionDef(Identifier id)
	{
		this(id, new LinkedList<Identifier>(), new StatementsBlock());
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
	
	public void addStatement(AbsStatement stmt)
	{
		_stmtsBlock.addStatement(stmt);
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
