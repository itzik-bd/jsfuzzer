package Main;

import java.io.File;
import java.io.IOException;

import Engines.EnginesUtil;
import Generator.Generator;
import Generator.Config.Configs;
import JST.Program;
import JST.Vistors.JstToJs;
import JST.Vistors.JstToTree;
import Utils.FilesIO;
import Utils.OutLog;
import Utils.TimerRunner;

public class JsFuzzer
{
	private JsFuzzerArgs _args;
	
	public static void main(String... args)
	{
		// parse arguments
		JsFuzzerArgs parsedArgs = JsFuzzerArgs.parse(args);
		
		// check that no errors occured while parsing JsFuzzer arguments
		if (parsedArgs != null)
		{
			// instantiate new fuzzer and run
			JsFuzzer fuzzer = new JsFuzzer(parsedArgs);
			fuzzer.run();
		}
		
		OutLog.printInfo("Bye!");
	}
	
	public JsFuzzer(JsFuzzerArgs args) {
		_args = args;
	}
	
	private void run()
	{
		// print help if help flag was up or no js file was supplied
		if (_args.showHelpAndExit() || _args.jsFile() == null) {
			System.out.println(JsFuzzerArgs.getUsageString());
			return;
		}
		
		// run JsFuuzer and measure total runtime
		TimerRunner<Void> worker = new TimerRunner<Void>()
		{
			@Override public Void run()
			{
				boolean programFlag = true;
				
				// check whether to generate a new program
				if (_args.isGenerate()) {
					programFlag = generate();
				}
				
				// run js program over engines if user asked for it
				if (programFlag && _args.runEngines()) {
					runEngines();
				}
				
				return null;
			}
		};
		
		OutLog.printInfo(String.format("All done. Execution time: %.2f sec", worker.lastRuntime()));
	}

	private void runEngines()
	{
		// create browser launcher
		try
		{
			String jsFilename = new File(_args.jsFile()).getName();
			String launcherCode = FilesIO.getSnippet("browserLauncher").replace("{FILENAME}", jsFilename);
			Utils.FilesIO.WriteToFile(_args.jsFile()+".html", launcherCode);
			OutLog.printInfo(String.format("Created launcher for js file '%s'", _args.jsFile()+".html"));
		}
		catch (IOException e) { OutLog.printError("Could not create browser launcher"); }
		
		EnginesUtil engines = new EnginesUtil();
		engines.compare(new File(_args.jsFile()), _args.runTimeout());
	}

	private boolean generate()
	{
		// try to generate program, and in case of exception - print the error
		try
		{
			// load configuration file
			Configs configs = Configs.loadConfigFile(_args.configsFile());
			OutLog.printInfo("Configuration file was successfully loaded");
			
			// generate program
			Generator gen = new Generator(configs, _args.seed(), _args.execFlow());
			Program program = gen.createProgram();
			String jsProgram = JstToJs.executeCostum(program, "\t", "\n");
			String jsVerbose = gen.getVerboseOutput();
			String jsTree = JstToTree.execute(program);
			double sizeKb = jsProgram.length() / 1024.0;
			OutLog.printInfo(String.format("New random program was successfully generated (%.2f Kb)", sizeKb));
			
			// save program as Javascript file
			Utils.FilesIO.WriteToFile(_args.jsFile(), jsProgram);
			OutLog.printInfo(String.format("The generated program was successfully saved to '%s'", _args.jsFile()));
			
			// save verbose to file
			Utils.FilesIO.WriteToFile(_args.jsFile()+".verbose", jsVerbose);
			OutLog.printInfo(String.format("The verbose was successfully saved to '%s'", _args.jsFile()+".verbose"));
			
			// save tree to file
			Utils.FilesIO.WriteToFile(_args.jsFile()+".tree", jsTree);
			OutLog.printInfo(String.format("The program tree was successfully saved to '%s'", _args.jsFile()+".tree"));
			
			// make sure that verbose and tree are identical
			if (!jsTree.equals(jsVerbose)) {
				OutLog.printWarn("verbose tree and actual tree are not identical!");
			}
		}
		catch (Exception e)
		{
			OutLog.printError("An error occurred: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}