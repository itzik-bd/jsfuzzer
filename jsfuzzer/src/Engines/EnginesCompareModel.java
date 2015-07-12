package Engines;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import Utils.FilesIO;

public class EnginesCompareModel
{
	Map<String,RunEngineResult> failed = new HashMap<String,RunEngineResult>();
	Map<String,RunEngineResult> passed = new HashMap<String,RunEngineResult>();
	Map<String,RunEngineResult> all = new HashMap<String,RunEngineResult>();
	
	Map<String,List<String>> eqvClasses = new HashMap<String,List<String>>(); 
	
	public void addEngineResult(String platformName, RunEngineResult result)
	{
		// add result to all map
		all.put(platformName, result);
		
		// check if error occurred (pass <=> sderr is empty) and add it to failes/passed map
		Map<String,RunEngineResult> map = result.getStderr().equals("") ? passed : failed;
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
	
	public void saveOutputByEquivalenceClass(String file)
	{
		for (Entry<String,List<String>> engineClass : eqvClasses.entrySet())
		{
			String stdout = engineClass.getKey();
			List<String> engineList = engineClass.getValue();
			
			try {
				FilesIO.WriteToFile(generateOutFileName(file, engineList), stdout);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String generateOutFileName(String file, List<String> engines)
	{
		return String.format("%s.%s.out", file, String.join(".", engines));
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
}