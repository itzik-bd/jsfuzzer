package JST;

import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.ProgramUnit;
import JST.Interfaces.Visitor;

public class StatementsBlock extends AbsStatement
{
	private List<ProgramUnit> _statements = new LinkedList<ProgramUnit>();
	

	public List<ProgramUnit> getStatements() 
	{
		return _statements;
	}
	
	public void addStatement(ProgramUnit s) 
	{
		_statements.add(s);
	}
	
	public void addStatementAtIndex(int index, ProgramUnit s) 
	{
		_statements.add(index, s);
	}
	
	
	@Override
	public Object accept(Visitor visitor, Object context) 
	{
		return visitor.visit(this, context);
	}
}