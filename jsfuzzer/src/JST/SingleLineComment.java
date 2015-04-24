package JST;

public class SingleLineComment extends AbstractComment
{
	private String _comment;
	
	public SingleLineComment(String comment)
	{
		_comment = comment;
	}
	
	public String getComment()
	{
		return _comment;
	}
}
