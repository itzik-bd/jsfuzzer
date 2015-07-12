package JST;

import JST.Interfaces.ProgramUnit;
import JST.Interfaces.Visitor;

public class Comment extends JSTNode implements ProgramUnit
{
	private String _comment;
	private boolean _forceExpand = false;
	
	public Comment(String comment)
	{
		_comment = comment;
		super.setNoneRandomNode();
	}
	
	public Comment(String comment, boolean forceExpand)
	{
		this(comment);
		_forceExpand = forceExpand;
	}
	
	public String getComment()
	{
		return _comment;
	}
	
	public boolean getForcedExpand()
	{
		return _forceExpand;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}
}