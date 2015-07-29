package Engines;

import java.io.File;
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
	
	public void compare(File file, int timeoutMilliseconds)
	{
		EnginesCompareModel compareModel = runFileAndGetCompareModel(file, timeoutMilliseconds);
		
		// print passed and failed engines
		OutLog.printInfo("Failed: " + String.join(", ", compareModel.getFailedEngines()));
		OutLog.printInfo("Passed: " + String.join(", ", compareModel.getPassedEngines()));
		OutLog.printInfo("Timeout: " + String.join(", ", compareModel.getTimeoutEngines()));
		
		// print Equivalence classed based on output
		for (Entry<String, List<String>> eqvClass : compareModel.getEquivalenceEngines().entrySet())
		{
			double sizeKb = eqvClass.getKey().length() / 1024.0;
			OutLog.printInfo(String.format("equivalence class (%.2f Kb): %s", sizeKb, String.join(", ", eqvClass.getValue())));
		}
		
		// print all stdout of engines that failed 
		for (Entry<String,String> entry : compareModel.getFailedErrors().entrySet())
		{
			OutLog.printDebug(entry.getKey()+" stderr:", entry.getValue());
		}
	}
	
	private EnginesCompareModel runFileAndGetCompareModel(File file, int timeoutMilliseconds)
	{
		EnginesCompareModel compareModel = new EnginesCompareModel();
		
		for (AbstractEngine engine : _engineList)
		{
			OutLog.printInfo(String.format("Running file '%s' over engine '%s'", file, engine.getPlatformName()));
			RunEngineResult model = engine.runFile(file, timeoutMilliseconds);
			OutLog.appendLastLine(String.format("; runtime: %.2f sec", model.getActualRuntime()));
			compareModel.addEngineResult(engine.getPlatformName(), model);
		}
		
		compareModel.removeLastExecutionEnginesOutput(file);
		compareModel.saveOutputByEquivalenceClass(file);
		
		return compareModel;
	}
}