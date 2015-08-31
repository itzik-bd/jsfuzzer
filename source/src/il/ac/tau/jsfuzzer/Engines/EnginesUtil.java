package il.ac.tau.jsfuzzer.Engines;

import il.ac.tau.jsfuzzer.Utils.OutLog;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class EnginesUtil
{ 
	private final List<AbstractEngine> _engineList = new LinkedList<AbstractEngine>();
	
	public static EnginesUtil create(File runEnginesFolder)
	{
		// check that engines folder exists
		if (!runEnginesFolder.isDirectory())
		{
			OutLog.printError(String.format("Could not find engines main directory: %s", runEnginesFolder));
			return null;
		}
		
		// create engine utils instance
		EnginesUtil enginesUtilsInstance = new EnginesUtil(runEnginesFolder);
		
		// check that at least one engine exist
		if (enginesUtilsInstance._engineList.size() == 0)
		{
			OutLog.printError(String.format("No engines were loaded", runEnginesFolder));
			return null;
		}
		else
		{
			OutLog.printInfo(String.format("Loaded %d engines", enginesUtilsInstance._engineList.size()));
		}
		
		return enginesUtilsInstance;
	}
	
	private EnginesUtil(File runEnginesFolder)
	{
		appendEngine(new SpiderMonkeyEngine(runEnginesFolder));
		appendEngine(new NodejsEngine(runEnginesFolder));
		appendEngine(new RhinoEngine(runEnginesFolder));
		appendEngine(new DynjsEngine(runEnginesFolder));
		appendEngine(new NashornEngine(runEnginesFolder));
	}
	
	private void appendEngine(AbstractEngine engine)
	{
		// add engine only if its files are OK
		if (engine.validateEngineDirectoryAndFiles()) {
			OutLog.printInfo(String.format("Successfully loaded engine %s", engine.getEngineName()));
			_engineList.add(engine);
		}
	}
	
	public void compare(File file, int timeoutMilliseconds)
	{
		EnginesCompareModel compareModel = runFileAndGetCompareModel(file, timeoutMilliseconds);
		OutLog.printSep();
		
		// print passed and failed engines
		OutLog.printInfo("Failed: " + String.join(", ", compareModel.getFailedEngines()));
		OutLog.printInfo("Passed: " + String.join(", ", compareModel.getPassedEngines()));
		OutLog.printInfo("Timeout: " + String.join(", ", compareModel.getTimeoutEngines()));
		OutLog.printSep();
		
		// print Equivalence classed based on output
		for (Entry<String, List<String>> eqvClass : compareModel.getEquivalenceEngines().entrySet())
		{
			double sizeKb = eqvClass.getKey().length() / 1024.0;
			OutLog.printInfo(String.format("equivalence class (%.2f Kb): %s", sizeKb, String.join(", ", eqvClass.getValue())));
		}
		OutLog.printSep();
		
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
			OutLog.printInfo(String.format("Running file '%s' over engine '%s'", file, engine.getEngineName()));
			RunEngineResult model = engine.runFile(file, timeoutMilliseconds);
			OutLog.appendLastLine(String.format("; runtime: %.2f sec", model.getActualRuntime()));
			compareModel.addEngineResult(engine.getEngineName(), model);
		}
		
		compareModel.removeLastExecutionEnginesOutput(file);
		compareModel.saveOutputByEquivalenceClass(file);
		
		return compareModel;
	}
}