package JST;

import java.util.HashMap;
import java.util.Map;

import JST.Interfaces.ObjectKeys;
import JST.Interfaces.Visitor;

public class ObjectExp extends AbsExpression
{
	private Map<ObjectKeys, AbsExpression> _map;
	
	public ObjectExp()
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
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}