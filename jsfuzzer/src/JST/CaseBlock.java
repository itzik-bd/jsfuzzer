package JST;

import java.util.List;

import JST.Interfaces.Caseable;
import JST.Interfaces.Visitor;

public class CaseBlock extends JSTNode
{
	private List<Caseable> _cases;
	private StatementsBlock _stmtsBlock;
	
	public CaseBlock(List<Caseable> cases, StatementsBlock stmtBlock)
	{
		_cases = cases;
		_stmtsBlock = stmtBlock;
	}
	
	public List<Caseable> getCases()
	{
		return _cases;
	}
	
	public StatementsBlock getStatementBlock()
	{
		return _stmtsBlock;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}