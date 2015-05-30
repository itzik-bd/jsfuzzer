package JST;

import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.Visitor;

public class FunctionDefinition extends AbsStatement
{
	private Identifier _id;
	private List<Identifier> _formals;
	private StatementsBlock _stmtsBlock;
	private int _paramsNum;
	
	public FunctionDefinition(Identifier id, List<Identifier> formals, StatementsBlock stmtsBlock, int paramsNum)
	{
		_id = id;
		_formals = formals;
		_stmtsBlock = stmtsBlock;
		_paramsNum = paramsNum;
	}
	
	public FunctionDefinition(Identifier id)
	{
		this(id, new LinkedList<Identifier>(), new StatementsBlock(), 0);
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
	
	public int getParamsNum()
	{
		return _paramsNum;
	}
	
	public void setParamsNum(int num)
	{
		_paramsNum = num;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}
