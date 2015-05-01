package JST;

import java.util.ArrayList;
import java.util.List;

import JST.Interfaces.Visitor;

/* Example:
 * [a,4+y,x/3]
 */
public class ArrayExpression extends AbsExpression
{
	private List<AbsExpression> _itemsList;
	
	public ArrayExpression()
	{
		_itemsList = new ArrayList<AbsExpression>();
	}
	
	public void addItem(AbsExpression exp)
	{
		_itemsList.add(exp);
	}
	
	public List<AbsExpression> getItemsList()
	{
		return _itemsList;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}
