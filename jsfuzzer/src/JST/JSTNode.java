package JST;

import JST.Interfaces.JSTObject;

public abstract class JSTNode implements JSTObject
{
	private boolean _nonRandomBranch = false;

	public boolean isNonRandomBranch()
	{
		return _nonRandomBranch;
	}
	
	public void setNonRandomBranch()
	{
		_nonRandomBranch = true;
	}
}