package Engines;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class EnginesCompareModel
{
	Map<String,RunEngineResult> failed = new HashMap<String,RunEngineResult>();
	Map<String,RunEngineResult> passed = new HashMap<String,RunEngineResult>();
	
	public void addEngineResult(String platformName, RunEngineResult result)
	{
		// check if error occurred (pass <=> sderr is empty)
		Map<String,RunEngineResult> map = result.getStderr().equals("") ? passed : failed;
		map.put(platformName, result);
	}
	
	public Collection<List<String>> getEquivalencePassedEngines()
	{
		Map<String,List<String>> eqvEngines = new HashMap<String,List<String>>();
		
		// iterate over all engines results
		for (Entry<String,RunEngineResult> resultEntry : passed.entrySet())
		{
			String engineOut = resultEntry.getValue().getStdout();
			String engineName = resultEntry.getKey();
			
			List<String> eqvClassList;
			
			// search if there already exists an match equivalence class
			if (eqvEngines.keySet().contains(engineOut)) {
				eqvClassList = eqvEngines.get(engineOut);
			}
			else {
				eqvClassList = new LinkedList<String>();
				eqvEngines.put(engineOut, eqvClassList);
			}
			
			// append engine to equivalence class
			eqvClassList.add(engineName);
		}
		
		return eqvEngines.values();
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