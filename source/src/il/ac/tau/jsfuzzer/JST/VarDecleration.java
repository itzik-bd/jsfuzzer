package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

import java.util.LinkedList;
import java.util.List;

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
