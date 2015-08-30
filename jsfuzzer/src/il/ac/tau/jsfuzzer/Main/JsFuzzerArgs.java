package il.ac.tau.jsfuzzer.Main;

import il.ac.tau.jsfuzzer.Generator.ExecFlow;
import il.ac.tau.jsfuzzer.Utils.FilesIO;
import il.ac.tau.jsfuzzer.Utils.OutLog;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsFuzzerArgs
{
	// var to hold js file path
	private File _jsFile = null;
	
	// vars for generating program
	private boolean _isGenerate = true;
	private String _configsFile = null;
	private String _seed = null;
	private ExecFlow _execFlow = ExecFlow.NORMAL;
	
	private boolean _showHelpAndExit = false;
	private File _runEnginesFolder = null;
	private int _runTimeout = 8000; // 8 seconds default
	
	// error - if not null error occurred
	private String _error = null;
	
	public static JsFuzzerArgs parse(String[] args)
	{
		JsFuzzerArgs parsedArgs;
		
		// if the program was invoked with "--json <FILE>" then parse arguments from json
		if (args.length > 1 && args[0].equals("--json"))
		{
			OutLog.printInfo(String.format("Loading JsFuzzer arguments from json file %s", args[1]));
			parsedArgs = new JsFuzzerArgs(args[1]);
		}
		else
			parsedArgs = new JsFuzzerArgs(args);
		
		if (parsedArgs._error != null) {
			OutLog.printError(parsedArgs._error);
			return null;
		}
		
		return 	parsedArgs;
	}
	
	private JsFuzzerArgs(String[] args)
	{
		int len = args.length;
		int i = 0;
		
		while (i<len)
		{
			if (args[i].equals("--help")) {
				argumentShowHelpAndExit();
			}
			else if (i+1 < len)
			{
				if (args[i].equals("--run")) {
					argumentRun(args[i+1]);
				}
				else if (args[i].equals("--out")) {
					argumentOut(args[i+1]);
				}
				else if (args[i].equals("--config")) {
					argumentConfig(args[i+1]);
				}
				else if (args[i].equals("--seed")) {
					argumentSeed(args[i+1]);
				}
				else if (args[i].equals("--execFlow")) {
					argumentExecFlow(args[i+1]);
				}
				else if (args[i].equals("--load")) {
					argumentLoad(args[i+1]);
				}
				else if (args[i].equals("--timeout")) {
					argumentTimeout(args[i+1]);
				}
				else {
					// we didn't used the next token
					i--;
				}
				
				// anyway advance the token, if no case was found it will be sum to zero
				i++;
			}
			
			// advance to next argument
			i++;
		}
	}

	private JsFuzzerArgs(String jsonArgFile)
	{
		// read json from file
		String jsonContent;
		try { jsonContent = FilesIO.ReadFile(jsonArgFile); }
		catch (IOException e) { _error = String.format("Could not read JsFuzzer arguments json file %s", jsonArgFile); return; }
		
		// parse json
		JSONParser parser = new JSONParser();
		JSONObject rootArguments = null;
		
		try { rootArguments = (JSONObject) parser.parse(jsonContent); }
		catch (ParseException e) { _error = "Could not parse arguments json file"; return; }
		
		try
		{
			for (Object keyObj : rootArguments.keySet())
			{
				String key = (String) keyObj;
				
				
				if (key.equals("help")) {
					argumentShowHelpAndExit();
				}
				else
				{
					String value = (String) rootArguments.get(keyObj);
					
					if (key.equals("run")) {
						argumentRun(value);
					}
					else if (key.equals("out")) {
						argumentOut(value);
					}
					else if (key.equals("config")) {
						argumentConfig(value);
					}
					else if (key.equals("seed")) {
						argumentSeed(value);
					}
					else if (key.equals("execFlow")) {
						argumentExecFlow(value);
					}
					else if (key.equals("load")) {
						argumentLoad(value);
					}
					else if (key.equals("timeout")) {
						argumentTimeout(value);
					}
				}
			}
		}
		catch (ClassCastException e) { _error = "In the arguments json file all values must be strings"; };
	}
	
	public static String getUsageString()
	{
		return "JsFuzzer version " + JsFuzzerConfigs.getVersion() + "\n\n"
				+ "usage: JsFuzzer [OPTIONS]\n"
				+ "--help           - show this help\n\n"
				+ "To generate new program:\n"
				+ "--out <FILE>     - save output to file\n"
				+ "--config <FILE>  - load costum configuration file\n"
				+ "--seed <SEED>    - set the seed of the random generator\n"
				+ "--execFlow <normal | extensive> - set the javascript execution print level\n\n"
				+ "To use a javascript file:\n"
				+ "--load <FILE>    - load javascript file\n\n"
				+ "To compare over supported engines:\n"
				+ "--run <ENGINES FOLDER> - runs generated program over engines, path to engines folder\n"
				+ "--timeout <MS>   - limit each javascript engine total runtime (milliseconds)\n";
	}
	
	// -------- Getters
	
	public File jsFile() {
		return _jsFile;
	}
	
	public boolean isGenerate() {
		return _isGenerate;
	}
	
	public String configsFile() {
		return _configsFile;
	}
	
	public String seed() {
		return _seed;
	}
	
	public ExecFlow execFlow() {
		return _execFlow;
	}
	
	public boolean showHelpAndExit() {
		return _showHelpAndExit;
	}
	
	public File runEnginesFolder() {
		return _runEnginesFolder;
	}
	
	public int runTimeout() {
		return _runTimeout;
	}
	
	// -------- Setters
	
	private void argumentOut(String outFile) {
		_jsFile = new File(outFile);
		_isGenerate = true;
	}
	
	private void argumentConfig(String configFile) {
		_configsFile = configFile;
	}
	
	private void argumentSeed(String seed) {
		_seed = seed;
	}
	
	private void argumentExecFlow(String execFlowStr) {
		switch (execFlowStr)
		{
		case "normal": _execFlow = ExecFlow.NORMAL; break;
		case "extensive": _execFlow = ExecFlow.EXTENSIVE; break;
		default:
			OutLog.printWarn("No such execFlow value: " + execFlowStr + ". Using execFlow: " + _execFlow);
		}
	}
	
	private void argumentLoad(String loadFile) {
		_jsFile  = new File(loadFile);
		_isGenerate = false; // no need to generate new program
	}
	
	private void argumentRun(String runEnginesFolder) {
		_runEnginesFolder = new File(runEnginesFolder);
	}
	
	private void argumentTimeout(String timeoutStr) {
		_runTimeout = Integer.parseInt(timeoutStr);
	}
	
	private void argumentShowHelpAndExit() {
		_showHelpAndExit = true;
	}
}