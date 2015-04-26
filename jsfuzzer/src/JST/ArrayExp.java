package JST;

import java.util.ArrayList;
import java.util.List;

public class ArrayExp
{
	private List<AbsExpression> _arrayExp;
	
	public ArrayExp()
	{
		_arrayExp = new ArrayList<AbsExpression>();
	}
	
	public void addItem(AbsExpression exp)
	{
		_arrayExp.add(exp);
	}
}
