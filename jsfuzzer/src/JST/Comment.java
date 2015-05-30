package JST;

import JST.Interfaces.ProgramUnit;
import JST.Interfaces.Visitor;

public class Comment extends JSTNode implements ProgramUnit
{
	private String _comment;
	
	public Comment(String comment)
	{
		_comment = comment;
	}
	
	public String getComment()
	{
		return _comment;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}
}