package Generator;

public class Context
{
	private final Context _parentContext;
	
	private final SymTable _symTable;
	
	private final boolean _inLoop;
	private final boolean _inFunction;
	private final int _contextDepth;
	
	/**
	 * Constructor for root of the tree 
	 */
	public Context()
	{
		_parentContext = null;
		_symTable = new SymTable(null);
		_inLoop = false;
		_inFunction = false;
		_contextDepth = 0;
	}
	
	/**
	 * Constructor for inner node in the tree
	 * @param parent - the parent of the node
	 */
	public Context(Context parent)
	{
		_parentContext = parent;
		_symTable = new SymTable(parent._symTable);
		_inLoop = parent._inLoop;
		_inFunction = parent._inFunction;
		_contextDepth = parent._contextDepth + 1;
	}
	
	/**
	 * Constructor for inner node in the tree
	 * @param parent - the parent of the node
	 * @param inLoop - flag if it's a loop context 
	 */
	public Context(Context parent, Boolean inLoop, Boolean inFunction)
	{
		_parentContext = parent;
		_symTable = new SymTable(parent._symTable);
		_inLoop = (inLoop == null || parent._inLoop == true) ? parent._inLoop : inLoop;
		_inFunction = (inFunction == null || parent._inFunction == true) ? parent._inFunction: inFunction;
		_contextDepth = parent._contextDepth + 1;
	}
	
	public SymTable getSymTable()
	{
		return _symTable;
	}
	
	public boolean isInLoop()
	{
		return _inLoop;
	}
	
	public boolean isInFunction()
	{
		return _inFunction;
	}
	
	public int getDepth()
	{
		return _contextDepth;
	}
	
	public Context getParent()
	{
		return _parentContext;
	}
}
