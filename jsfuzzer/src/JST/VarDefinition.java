package JST;

import java.util.HashMap;
import java.util.Map;

public class VarDefinition extends AbsStatement
{
	private Map<Identifier, AbsExpression> _idInitMap;
	
	public VarDefinition()
	{
		_idInitMap = new HashMap<Identifier, AbsExpression>();
	}

	public void addIdInit(Identifier id, AbsExpression expr)
	{
		_idInitMap.put(id, expr);
	}

	public void addIdInit(Identifier id)
	{
		addIdInit(id, null);
	}
	
	public Map<Identifier, AbsExpression> getIdInitMap()
	{
		return _idInitMap;
	}
}
