package JST;

import java.util.LinkedList;
import java.util.List;

import JST.Interfaces.Visitor;

public class VarDecleration extends AbsStatement
{
	private List<VarDeclerator> _decleratorList;
	
	public VarDecleration()
	{
		_decleratorList = new LinkedList<VarDeclerator>();
	}

	public void addDeclerator(Identifier id, AbsExpression expr)
	{
		_decleratorList.add(new VarDeclerator(id, expr));
	}

	public void addDeclerator(Identifier id)
	{
		addDeclerator(id, null);
	}
	
	public List<VarDeclerator> getDecleratorList()
	{
		return _decleratorList;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
	
	public class VarDeclerator extends JSTNode
	{
		private Identifier _id;
		private AbsExpression _init; // can be null
		
		public VarDeclerator(Identifier id, AbsExpression init)
		{
			_id = id;
			_init = init;
		}
		
		public Identifier getId()
		{
			return _id;
		}
		
		public AbsExpression getInit()
		{
			return _init;
		}
		
		public boolean hasInit()
		{
			return (_init != null);
		}
		
		@Override
		public Object accept(Visitor visitor, Object context) {
			return visitor.visit(this, context);
		}
	}
}
