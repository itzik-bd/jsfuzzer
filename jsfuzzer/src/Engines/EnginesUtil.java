package Engines;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import Utils.OutLog;

public class EnginesUtil
{ 
	private final List<AbstractEngine> _engineList = new LinkedList<AbstractEngine>();
	
	public EnginesUtil()
	{
		_engineList.add(new SpiderMonkeyEngine());
		_engineList.add(new NodejsEngine());
		_engineList.add(new RhinoEngine());
		_engineList.add(new DynjsEngine());
		_engineList.add(new NashornEngine());
	}
	
	public void compare(String file)
	{
		EnginesCompareModel compareModel = runFileAndGetCompareModel(file);
		
		OutLog.printInfo("Failed: " + String.join(", ", compareModel.getFailedEngines()));
		OutLog.printInfo("Passed: " + String.join(", ", compareModel.getPassedEngines()));
		
		for (List<String> eqvClass : compareModel.getEquivalencePassedEngines())
		{
			OutLog.printInfo("equivalent class: " + String.join(", ", eqvClass));
		}
		
		for (Entry<String,String> entry : compareModel.getFailedErrors().entrySet())
		{
			OutLog.printDebug(entry.getKey()+" stderr:", entry.getValue());
		}
	}
	
	private EnginesCompareModel runFileAndGetCompareModel(String file)
	{
		EnginesCompareModel compareModel = new EnginesCompareModel();
		
		for (AbstractEngine engine : _engineList)
		{
			OutLog.printInfo(String.format("Running file '%s' over engine '%s'", file, engine.getPlatformName()));
			compareModel.addEngineResult(engine.getPlatformName(), engine.runFile(file));
		}
		
		return compareModel;
	}
}