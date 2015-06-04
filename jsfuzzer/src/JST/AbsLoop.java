package JST;

public abstract class AbsLoop extends AbsStatement
{
	private StatementsBlock _stmtsBlock;
	
	/** The loopCounter - Our solution to the HALT problem */
	private VarDecleration _loopCounterInit; 
	
	public AbsLoop(StatementsBlock stmtsBlock, VarDecleration loopCounterInit)
	{
		_stmtsBlock = stmtsBlock;
		_loopCounterInit = loopCounterInit;
	}
	
	public StatementsBlock getStatementsBlock()
	{
		return _stmtsBlock;
	}
	
	public VarDecleration getLoopCounterInit ()
	{
		return _loopCounterInit;
	}

	public void setLoopCounterInit(VarDecleration loopCounterInit)
	{
		_loopCounterInit = loopCounterInit;
	}
}
