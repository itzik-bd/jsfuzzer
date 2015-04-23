package JST;

import java.util.LinkedList;
import java.util.List;

public class StatementsBlock extends AbstractStatement
{
	private List<AbstractStatement> _statements = new LinkedList<AbstractStatement>();
	
	public List<AbstractStatement> getStatements() {
		return _statements;
	}
	
	public void addStatement(AbstractStatement s) {
		_statements.add(s);
	}
}
