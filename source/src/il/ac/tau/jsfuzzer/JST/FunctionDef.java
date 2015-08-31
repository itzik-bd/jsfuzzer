package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

import java.util.List;

public class FunctionDef extends AbsStatement
{
	private Identifier _id;
	private List<Identifier> _formals;
	private StatementsBlock _stmtsBlock;
	private Call _registerFunctionInRuntimeCall;
	
	public FunctionDef(Identifier id, List<Identifier> formals, StatementsBlock stmtsBlock, Call registerFunctionInRuntimeCall)
	{
		_id = id;
		_formals = formals;
		_stmtsBlock = stmtsBlock;
		_registerFunctionInRuntimeCall = registerFunctionInRuntimeCall;
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
	
	public Call getRegisterFunctionInRuntimeCall()
	{
		return _registerFunctionInRuntimeCall;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}
