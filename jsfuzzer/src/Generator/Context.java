package Generator;

public class Context
{
	private final Context _parentContext;
	
	private boolean _inWhileLoop;
	private final int _contextDepth;
	
	/**
	 * Constructor for root of the tree 
	 */
	public Context()
	{
		_parentContext = null;
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
		_inWhileLoop = parent._inWhileLoop;
		_contextDepth = parent._contextDepth + 1;
	}
	
	public void setInWhileLoop()
	{
		_inWhileLoop = true;
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
