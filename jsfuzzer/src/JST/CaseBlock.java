package JST;

import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.Caseable;
import JST.Interfaces.Visitor;

	public class CaseBlock extends JSTNode
	{
		private List<Caseable> _cases;
		
		private List<AbsStatement> _statements;
		
		public CaseBlock(List<Caseable> cases, List<AbsStatement> statements)
		{
			_cases = cases;
			_statements = statements;
		}
					
		public CaseBlock(List<Caseable> cases)
		{
			_cases = cases;
			_statements = new LinkedList<AbsStatement>();
		}
		
		public void addStatement(AbsStatement stmt)
		{
			_statements.add(stmt);
		}
		
		public List<Caseable> getCases()
		{
			return _cases;
		}
		
		public List<AbsStatement> getStatements()
		{
			return _statements;
		}
		
		@Override
		public Object accept(Visitor visitor, Object context) {
			return visitor.visit(this, context);
		}
	}
