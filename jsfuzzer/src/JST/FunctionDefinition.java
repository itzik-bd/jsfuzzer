package JST;

import java.util.LinkedList;
import java.util.List;

public class FunctionDefinition extends AbsStatement
{
	private Identifier _id;
	private List<Identifier> _formals;
	private List<AbsStatement> _statements;
	
	public FunctionDefinition(Identifier id, List<Identifier> formals)
	{
		_id = id;
		_formals = formals;
		_statements = new LinkedList<AbsStatement>();
	}
	
	public FunctionDefinition(Identifier id)
	{
		this(id, new LinkedList<Identifier>());
	}
	
	public Identifier getId()
	{
		return _id;
	}
	
	public List<Identifier> getFormals()
	{
		return _formals;
	}
	
	public boolean hasFormals()
	{
		return (_formals != null);
	}
	
	public List<AbsStatement> getStatements()
	{
		return _statements;
	}
	
	public void addStatement(AbsStatement stmt)
	{
		_statements.add(stmt);
	}
}
