package il.ac.tau.jsfuzzer.Main;

import il.ac.tau.jsfuzzer.Engines.EnginesUtil;
import il.ac.tau.jsfuzzer.Generator.Generator;
import il.ac.tau.jsfuzzer.Generator.Config.Configs;
import il.ac.tau.jsfuzzer.JST.Program;
import il.ac.tau.jsfuzzer.JST.Vistors.JstToJs;
import il.ac.tau.jsfuzzer.JST.Vistors.JstToTree;
import il.ac.tau.jsfuzzer.Utils.FilesIO;
import il.ac.tau.jsfuzzer.Utils.OutLog;
import il.ac.tau.jsfuzzer.Utils.TimerRunner;

import java.io.File;
import java.io.IOException;

public class JsFuzzer
{
	private JsFuzzerArgs _args;
	
	public static void main(String... args)
	{
		// parse arguments
		JsFuzzerArgs parsedArgs = JsFuzzerArgs.parse(args);
		
		// check that no errors occurred while parsing JsFuzzer arguments
		if (parsedArgs != null)
		{
			// print help if help flag was up or no js file was supplied
			if (parsedArgs.showHelpAndExit() || parsedArgs.jsFile() == null) {
				System.out.println(JsFuzzerArgs.getUsageString());
				return;
			}
			
			// instantiate new fuzzer and run
			OutLog.printSep();
			JsFuzzer fuzzer = new JsFuzzer(parsedArgs);
			fuzzer.run();
			OutLog.printInfo("Bye!");
			OutLog.printNewLine();
		}
	}
	
	public JsFuzzer(JsFuzzerArgs args) {
		_args = args;
	}
	
	private void run()
	{
		// run JsFuuzer and measure total runtime
		TimerRunner<Void> worker = new TimerRunner<Void>()
		{
			@Override public Void run()
			{
				boolean programFlag = true;
				
				// check whether to generate a new program
				if (_args.isGenerate())
				{
					programFlag = generate();
				}
				else if (!_args.jsFile().exists())
				{
					OutLog.printError(String.format("Could not load javascript file %s", _args.jsFile().getPath()));
					programFlag = false;
				}
				OutLog.printSep();
				
				// run js program over engines if user asked for it
				if (programFlag && _args.runEnginesFolder() != null) {
					runEngines();
					OutLog.printSep();
				}
				
				return null;
			}
		};
		
		OutLog.printInfo(String.format("All done. Execution time: %.2f sec", worker.lastRuntime()));
	}

	private void runEngines()
	{
		EnginesUtil engines = EnginesUtil.create(_args.runEnginesFolder());
		OutLog.printSep();
		if (engines != null) {
			engines.compare(_args.jsFile(), _args.runTimeout());
		}
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
			FilesIO.WriteToFile(_args.jsFile(), jsProgram);
			OutLog.printInfo(String.format("The generated program was successfully saved to '%s'", _args.jsFile().getPath()));
			
			// save verbose to file
			File verboseFile = FilesIO.getExtendedFile(_args.jsFile(), ".verbose");
			FilesIO.WriteToFile(verboseFile, jsVerbose);
			OutLog.printInfo(String.format("The verbose was successfully saved to '%s'", verboseFile.getPath()));
			
			// save tree to file
			File treeFile = FilesIO.getExtendedFile(_args.jsFile(), ".tree");
			FilesIO.WriteToFile(treeFile, jsTree);
			OutLog.printInfo(String.format("The program tree was successfully saved to '%s'", treeFile.getPath()));
			
			// make sure that verbose and tree are identical
			if (!jsTree.equals(jsVerbose)) {
				OutLog.printWarn("verbose tree and actual tree are not identical!");
			}
			
			// create browser launcher
			try
			{
				String launcherCode = FilesIO.getSnippet("browserLauncher").replace("{FILENAME}", _args.jsFile().getName());
				File launcherFile = FilesIO.getExtendedFile(_args.jsFile(), ".html");
				il.ac.tau.jsfuzzer.Utils.FilesIO.WriteToFile(launcherFile, launcherCode);
				OutLog.printInfo(String.format("Created launcher for js file '%s'", launcherFile.getPath()));
			}
			catch (IOException e) { OutLog.printError("Could not create browser launcher"); }
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