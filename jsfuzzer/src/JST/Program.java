package JST;

import java.util.LinkedList;
import java.util.List;

public class Program extends JSTNode
{
	private List<AbsStatement> _statements = new LinkedList<AbsStatement>();
	
	public List<AbsStatement> getStatements() {
		return _statements;
	}
	
	public void addStatement(AbsStatement s) {
		_statements.add(s);
	}
}