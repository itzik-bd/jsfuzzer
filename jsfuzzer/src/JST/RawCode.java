package JST;

import JST.Interfaces.ProgramUnit;
import JST.Interfaces.Visitor;

public class RawCode extends JSTNode implements ProgramUnit
{
	private String _code;
	
	public RawCode(String code)
	{
		_code = code;
		super.setNoneRandomNode();
	}
	
	public String getCode()
	{
		return _code;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}
}