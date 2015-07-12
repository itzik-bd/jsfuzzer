package Main;

import Engines.EnginesUtil;
import Generator.Generator;
import Generator.Config.Configs;
import JST.Program;
import JST.Vistors.JstToJs;
import JST.Vistors.JstToTree;
import Utils.OutLog;

public class JsFuzzer
{
	// var to hold js file path
	private String _jsFile = null;
	
	// vars for generating program
	private boolean _isGenerate = true;
	private String _configsFile = null;
	private String _seed = null;
	
	private boolean _showHelpAndExit = false;
	private boolean _runEngines = false;
	
	public static void main(String... args)
	{
		// instantiate new fuzzer and run
		JsFuzzer fuzzer = new JsFuzzer(args);
		fuzzer.run();
	}
	
	public JsFuzzer(String[] args) {
		parseArguments(args);
	}
	
	public static String getUsageString()
	{
		return "usage: JsFuzzer [OPTIONS]\n"
				+ "--help           - show this help\n\n"
				+ "To generate new program:\n"
				+ "--out <FILE>     - save output to file\n"
				+ "--config <FILE>  - load costum configuration file\n"
				+ "--seed <SEED>    - set the seed of the random generator\n\n"
				+ "To use a javascript file:\n"
				+ "--load <FILE>    - load javascript file\n\n"
				+ "To compare over supported engines:\n"
				+ "--run            - runs generated program over engines\n";
	}
	
	public void parseArguments(String[] args)
	{
		int len = args.length;
		int i = 0;
		
		while (i<len)
		{
			if (args[i].equals("--out") && i+1 < len) {
				_jsFile = args[i+1];
				_isGenerate = true;
				i++;
			}
			else if (args[i].equals("--config") && i+1 < len) {
				_configsFile = args[i+1];
				i++;
			}
			else if (args[i].equals("--seed") && i+1 < len) {
				_seed  = args[i+1];
				i++;
			}
			else if (args[i].equals("--load") && i+1 < len) {
				_jsFile  = args[i+1];
				_isGenerate = false; // no need to generate new program
				i++;
			}
			else if (args[i].equals("--run")) {
				_runEngines = true;
			}
			else if (args[i].equals("--help")) {
				_showHelpAndExit = true;
			}
			
			// advance to next argument
			i++;
		}
		
		// show help if no path were chosen
		if (_jsFile==null) {
			_showHelpAndExit = true;
		}
	}
	
	private void run()
	{
		// check if the user asked for help
		if (_showHelpAndExit) {
			System.out.println(getUsageString());
			return;
		}
		
		// check that the client supplied output file
		if (_jsFile == null) {
			System.out.println("Please specify an output file with -o <FILE>");
			return;
		}
		
		long startTime = System.currentTimeMillis();
		
		// check whether to generate a new program
		if (_isGenerate) {
			generate();
		}
		
		// run js program over engines if user asked for it
		if (_runEngines) {
			runEngines();
		}
		
		long endTime = System.currentTimeMillis();
		double totalTimeSeconds = (endTime - startTime) / 1000.0;
		
		OutLog.printInfo(String.format("All done. Execution time: %.2f sec", totalTimeSeconds));
	}

	private void runEngines()
	{
		if (_runEngines)
		{
			EnginesUtil engines = new EnginesUtil();
			engines.compare(_jsFile);	
		}
	}

	private void generate()
	{
		// try to generate program, and in case of exception - print the error
		try
		{
			// load configuration file
			Configs configs = Configs.loadConfigFile(_configsFile);
			OutLog.printInfo("Configuration file was successfully loaded");
			
			// generate program
			Generator gen = new Generator(configs, _seed);
			Program program = gen.createProgram();
			String jsProgram = JstToJs.executeCostum(program, "  ", "\n");
			String jsVerbose = gen.getVerboseOutput();
			String jsTree = JstToTree.execute(program);
			double sizeKb = jsProgram.length() / 1024.0;
			OutLog.printInfo(String.format("New random program was successfully generated (%.2fKb)", sizeKb));
			
			// save program as Javascript file
			Utils.FilesIO.WriteToFile(_jsFile, jsProgram);
			OutLog.printInfo(String.format("The generated program was successfully saved to '%s'", _jsFile));
			
			// save verbose to file
			Utils.FilesIO.WriteToFile(_jsFile+".verbose", jsVerbose);
			OutLog.printInfo(String.format("The verbose was successfully saved to '%s'", _jsFile+".verbose"));
			
			// save tree to file
			Utils.FilesIO.WriteToFile(_jsFile+".tree", jsTree);
			OutLog.printInfo(String.format("The program tree was successfully saved to '%s'", _jsFile+".tree"));
			
			// make sure that verbose and tree are identical
			if (!jsTree.equals(jsVerbose)) {
				OutLog.printWarn("verbose tree and actual tree are not identical!");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}