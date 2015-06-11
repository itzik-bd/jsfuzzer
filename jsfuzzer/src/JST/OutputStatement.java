package JST;

import JST.Enums.OutputType;
import JST.Interfaces.Visitor;

public class OutputStatement extends AbsStatement 
{
	private AbsExpression _exp;
	private OutputType _type;
	
	public OutputStatement(AbsExpression exp, OutputType type)
	{
		_exp = exp;
		_type = type;
	}
	
	public String toString()
	{
		switch (_type)
		{
			case OUT_ALERT: return ("window.alert(" + _exp.toString() + ")");
			case OUT_LOG: return ("console.log(" + _exp.toString() + ")");
			case LIT_DOCUMENT: return ("document.write(" + _exp.toString() + ")");
			default: return (_exp.toString());
		}
	}

	@Override
	public Object accept(Visitor visitor, Object context)
	{
		return visitor.visit(this, context);
	}
}