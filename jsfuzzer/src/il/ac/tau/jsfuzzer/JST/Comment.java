package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.ProgramUnit;
import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

public class Comment extends JSTNode implements ProgramUnit
{
	private String _comment;
	private boolean _forceExpand = false;
	private int _linesBefore = 0;
	private int _linesAfter = 0;
	
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
	
	public Comment(String comment, boolean forceExpand, int linesBefore, int linesAfter)
	{
		this(comment, forceExpand);
		this._linesBefore = linesBefore;
		this._linesAfter = linesAfter;
	}
	
	public Comment(String comment, int linesBefore, int linesAfter)
	{
		this(comment);
		this._linesBefore = linesBefore;
		this._linesAfter = linesAfter;
	}
	
	
	public String getComment()
	{
		return _comment;
	}
	
	public boolean getForcedExpand()
	{
		return _forceExpand;
	}
	
	public int getLinesBefore()
	{
		return _linesBefore;
	}
	
	public int getLinesAfter()
	{
		return _linesAfter;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}
}