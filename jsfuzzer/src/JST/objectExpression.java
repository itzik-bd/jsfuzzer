package JST;

import java.util.HashMap;
import java.util.Map;

import JST.Interfaces.ObjectKeys;

public class objectExpression
{
	private Map<ObjectKeys, AbsExpression> _map;
	
	public objectExpression()
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
}
