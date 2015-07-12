package JST;

import java.util.Arrays;
import java.util.List;

import JST.Interfaces.Visitor;

/* Example:
 * [a,4+y,x/3]
 */
public class ArrayExp extends AbsExpression
{
	private List<AbsExpression> _itemsList;
	
	public ArrayExp(List<AbsExpression> itemsList)
	{
		_itemsList = itemsList;
	}
	
	public ArrayExp(AbsExpression... itemsList)
	{
		_itemsList = Arrays.asList(itemsList);
	}
	
	public List<AbsExpression> getItemsList()
	{
		return _itemsList;
	}

	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}
