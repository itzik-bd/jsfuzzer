package JST;

import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.Visitor;

public class StatementsBlock extends AbsStatement
{
	private List<AbsStatement> _statements = new LinkedList<AbsStatement>();
	
	public List<AbsStatement> getStatements() {
		return _statements;
	}
	
	public void addStatement(AbsStatement s) {
		_statements.add(s);
	}
	
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}