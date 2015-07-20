package JST;

import JST.Interfaces.JSTObject;

public abstract class JSTNode implements JSTObject
{
	private static int _instanceCounter = 0;
	protected final int _uniqueId = _instanceCounter++;
	
	/** True if the branch under this node is randomized */
	private boolean _isRandomBranch = true;
	
	/** True if the node is randomized */
	private boolean _isRandomNode = true;

	public boolean isRandomBranch()
	{
		return _isRandomBranch;
	}
	
	public void setNoneRandomBranch()
	{
		_isRandomBranch = false;
	}
	
	public boolean isRandomNode()
	{
		return _isRandomNode;
	}
	
	public void setNoneRandomNode()
	{
		_isRandomNode = false;
	}
	
	public int getUniqueId()
	{
		return _uniqueId;
	}
}