package JST;

import java.util.HashMap;
import java.util.Map;

import JST.Interfaces.ObjectKeys;
import JST.Interfaces.Visitor;

public class ObjectExpression extends AbsExpression
{
	private Map<ObjectKeys, AbsExpression> _map;
	
	public ObjectExpression()
	{
		_map = new HashMap<ObjectKeys, AbsExpression>();
	}
	
	public Map<ObjectKeys, AbsExpression> getMap()
	{
		return _map;
	}
	
	public void addToMap(ObjectKeys key, AbsExpression exp)
	{
		_map.put(key, exp);
	}
	
	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}