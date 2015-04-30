package JST;

import java.util.ArrayList;
import java.util.List;

/* Example:
 * [a,4+y,x/3]
 */
public class ArrayExp extends AbsExpression
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
