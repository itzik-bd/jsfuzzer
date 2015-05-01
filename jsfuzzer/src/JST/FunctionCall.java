package JST;

import java.util.LinkedList;
import java.util.List;

public class FunctionCall extends AbsExpression 
{
	private Identifier _id;
	private List<AbsExpression> _formals;
	
	public FunctionCall(Identifier id, List<AbsExpression> formals)
	{
		_id = id;
		_formals = formals;
	}
	
	public FunctionCall(Identifier id)
	{
		this(id, new LinkedList<AbsExpression>());
	}
	
	public Identifier getId()
	{
		return _id;
	}
	
	public List<AbsExpression> getFormals()
	{
		return _formals;
	}
	
	public boolean hasFormals()
	{
		return (_formals != null);
	}
}
