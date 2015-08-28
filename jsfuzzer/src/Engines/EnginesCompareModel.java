package Engines;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import Utils.FilesIO;
import Utils.OutLog;

public class EnginesCompareModel
{
	Map<String,RunEngineResult> failed = new HashMap<String,RunEngineResult>();
	Map<String,RunEngineResult> passed = new HashMap<String,RunEngineResult>();
	Map<String,RunEngineResult> timeout = new HashMap<String,RunEngineResult>();
	Map<String,RunEngineResult> all = new HashMap<String,RunEngineResult>();
	
	Map<String,List<String>> eqvClasses = new HashMap<String,List<String>>(); 
	
	public void addEngineResult(String platformName, RunEngineResult result)
	{
		// add result to all map
		all.put(platformName, result);
		
		// add result to the right map: timeout/pass/error
		// (error <=> sderr is empty)
		Map<String,RunEngineResult> map = result.isTimeExceeded() ? timeout : (result.getStderr().equals("") ? passed : failed);
		map.put(platformName, result);
		
		// add result to equivalence class
		addResultToEqvClass(platformName, result);
	}
	
	private void addResultToEqvClass(String engineName, RunEngineResult result)
	{
		String engineOut = result.getStdout();
		List<String> eqvClassList;
		
		// search if there already exists an match equivalence class
		if (eqvClasses.keySet().contains(engineOut)) {
			eqvClassList = eqvClasses.get(engineOut);
		}
		else {
			eqvClassList = new LinkedList<String>();
			eqvClasses.put(engineOut, eqvClassList);
		}
		
		// append engine to equivalence class
		eqvClassList.add(engineName);
	}
	
	public Map<String,List<String>> getEquivalenceEngines()
	{		
		return eqvClasses;
	}
	
	public void saveOutputByEquivalenceClass(File file)
	{
		for (Entry<String,List<String>> engineClass : eqvClasses.entrySet())
		{
			String stdout = engineClass.getKey();
			List<String> engineList = engineClass.getValue();
			
			try { FilesIO.WriteToFile(generateEngineOutFileName(file, engineList), stdout); }
			catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public void removeLastExecutionEnginesOutput(final File file)
	{
		File[] myFiles = file.getParentFile().listFiles(new FilenameFilter() {
		    public boolean accept(File currentDirectory, String currFile) {
		        return (currFile.startsWith(file.getName()+".engines") && currFile.endsWith(".out"));
		    }
		});
		
		if (myFiles.length > 0)
		{
			OutLog.printInfo("remove old engines out files");
			
			for(File engineOutFile : myFiles)
				engineOutFile.delete();
		}
			
	}
	
	private String generateEngineOutFileName(File file, List<String> engines)
	{
		return String.format("%s.engines.%s.out", file, String.join(".", engines));
	}
	
	public Map<String,String> getFailedErrors()
	{
		Map<String,String> map = new HashMap<String,String>();
		
		for (Entry<String,RunEngineResult> entry : failed.entrySet())
		{
			map.put(entry.getKey(), entry.getValue().getStderr());
		}
		
		return map;
	}
	
	public Set<String> getFailedEngines()
	{
		return failed.keySet();
	}
	
	public Set<String> getPassedEngines()
	{
		return passed.keySet();
	}
	
	public Set<String> getTimeoutEngines()
	{
		return timeout.keySet();
	}
}