package JST;

import java.util.LinkedList;
import java.util.List;

public class MultiLineComment extends AbstractComment
{
	private List<String> _commentLines;
	
	public MultiLineComment()
	{	
		_commentLines = new LinkedList<String>();
	}
	
	public MultiLineComment(List<String> commentLines)
	{
		_commentLines = commentLines;
	}
	
	public void addCommentLine(String commentLine)
	{
		_commentLines.add(commentLine);
	}
	
	public List<String> getCommentLines()
	{
		return _commentLines;
	}
}
