package Main;

import Engines.EnginesUtil;
import Generator.Generator;
import Generator.Config.Configs;
import JST.Program;
import JST.Vistors.JstToJs;
import JST.Vistors.JstToTree;

public class JsFuzzer
{
	private String _outputFile = null;
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
		return "usage: JsFuzzer -o <FILE> [OPTIONS]\n"
				+ "-o <FILE> - save output to file\n"
				+ "-c <FILE> - load costum configuration file\n"
				+ "-s <SEED> - set the seed of the random generator\n"
				+ "-r        - runs generated program over engines";
	}
	
	public void parseArguments(String[] args)
	{
		int len = args.length;
		int i = 0;
		
		while (i<len)
		{
			if (args[i].equals("-o") && i+1 < len) {
				_outputFile = args[i+1];
				i++;
			}
			else if (args[i].equals("-c") && i+1 < len) {
				_configsFile = args[i+1];
				i++;
			}
			else if (args[i].equals("-s") && i+1 < len) {
				_seed  = args[i+1];
				i++;
			}
			else if (args[i].equals("-r")) {
				_runEngines = true;
			}
			else if (args[i].equals("--help")) {
				_showHelpAndExit = true;
			}
			
			// advance to next argument
			i++;
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
		if (_outputFile == null) {
			System.out.println("Please specify an output file with -o <FILE>");
			return;
		}
		
		// generate new file
		generate();
	}

	private void generate()
	{
		// try to generate program, and in case of exception - print the error
		try
		{
			// load configuration file
			Configs configs = Configs.loadConfigFile(_configsFile);
			System.out.println("Configuration file was successfully loaded");
			
			// generate program
			Generator gen = new Generator(configs, _seed);
			Program program = gen.createProgram();
			System.out.println("New random program was successfully generated");
			
			// save program as Javascript file					
			Utils.FilesIO.WriteToFile(_outputFile, JstToJs.executeCostum(program, "  ", "\n"));
			System.out.println(String.format("The generated program was successfully saved to '%s'", _outputFile));
			
			// save verbose to file
			Utils.FilesIO.WriteToFile(_outputFile+".verbose", gen.getVerboseOutput());
			System.out.println(String.format("The verbose was successfully saved to '%s'", _outputFile+".verbose"));
			
			// save tree to file
			Utils.FilesIO.WriteToFile(_outputFile+".tree", JstToTree.execute(program));
			System.out.println(String.format("The program tree was successfully saved to '%s'", _outputFile+".tree"));
			
			// run the new program over engines if client asked for it
			if (_runEngines) {
				EnginesUtil engines = new EnginesUtil();
				engines.runFile(_outputFile);	
			}
			
			System.out.println("All done.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}