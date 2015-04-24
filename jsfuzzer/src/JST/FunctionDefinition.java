package JST;

import java.util.LinkedList;
import java.util.List;

public class FunctionDefinition
{
	private List<AbstractExpression> _formals;
	
	/* 
	 * TODO: Might need to add function name
	 */
	
	private List<AbstractStatement> _statements;
	
	public FunctionDefinition(List<AbstractExpression> formals)
	{
		_formals = formals;
		_statements = new LinkedList<AbstractStatement>();
	}
	
	public FunctionDefinition()
	{
		this(null);
	}
	
	public List<AbstractExpression> getFormals()
	{
		return _formals;
	}
	
	public boolean hasFormals()
	{
		return (_formals != null);
	}
	
	public List<AbstractStatement> getStatements()
	{
		return _statements;
	}
	
	public void addStatement(AbstractStatement stmt)
	{
		_statements.add(stmt);
	}
	
}
