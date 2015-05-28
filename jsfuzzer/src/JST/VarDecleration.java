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

	public void addDeclerator(VarDeclerator varDeclerator)
	{
		_decleratorList.add(varDeclerator);
	}

	public List<VarDeclerator> getDecleratorList()
	{
		return _decleratorList;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}
