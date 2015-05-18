package Engines;

import java.util.LinkedList;
import java.util.List;

public class EnginesUtil
{ 
	private final List<AbstractEngine> _engineList = new LinkedList<AbstractEngine>();
	
	public EnginesUtil()
	{
		_engineList.add(new SpiderMonkeyEngine());
		_engineList.add(new NodejsEngine());
	}
	
	public List<AbstractEngine> getEngineList()
	{
		return _engineList;
	}
	
	public void runFile(String file)
	{
		for (AbstractEngine engine : _engineList)
		{
			engine.runFile(file);
		}
	}
}