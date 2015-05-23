package Generator;

public class Context
{
	private final Context _parentContext;
	
	private final SymTable _symTable;
	
	private final boolean _inWhileLoop;
	private final int _contextDepth;
	
	/**
	 * Constructor for root of the tree 
	 */
	public Context()
	{
		_parentContext = null;
		_symTable = new SymTable(null);
		_inWhileLoop = false;
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
		_inWhileLoop = parent._inWhileLoop;
		_contextDepth = parent._contextDepth + 1;
	}
	
	/**
	 * Constructor for inner node in the tree
	 * @param parent - the parent of the node
	 * @param inWhileLoop - flag if it's a loop context 
	 */
	public Context(Context parent, boolean inWhileLoop)
	{
		_parentContext = parent;
		_symTable = new SymTable(parent._symTable);
		_inWhileLoop = (parent._inWhileLoop == true) ? true : inWhileLoop;
		_contextDepth = parent._contextDepth + 1;
	}
	
	public SymTable getSymTable()
	{
		return _symTable;
	}
	
	public boolean isInWhileLoop()
	{
		return _inWhileLoop;
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
