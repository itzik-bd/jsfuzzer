package Main;
import java.io.IOException;

import Generator.Generator;
import Generator.Config.Configs;
import JST.Vistors.JstToJs;

public class JsFuzzer
{
	private String _outputFile = null;
	private String _configsFile = null;
	
	public static void main(String... args)
	{
		// if no arguments exists then print usage message and exit
//		if (args.length == 0) {
//			System.out.println(getUsageString());
//			return;
//		}
		
		// instantiate new fuzzer and run
		JsFuzzer fuzzer = new JsFuzzer(args);
		fuzzer.run();
	}

	public static String getUsageString()
	{
		return "usage: JsFuzzer -o <FILE> [OPTIONS]\n"
				+ "-o [FILE] - save output to file\n"
				+ "-c [FILE] - load costum configuration file\n";
	}
	
	public JsFuzzer(String[] args) {
		parseArguments(args);
	}
	
	public void parseArguments(String[] args)
	{
		int len = args.length;
		int i = 0;
		
		while (i<len)
		{
			if (args[i] == "-o" && i+1 < len) {
				_outputFile = args[i+1];
				i++;
			}
			else if (args[i] == "-o" && i+1 < len) {
				_configsFile = args[i+1];
				i++;
			}
			
			// advance to next argument
			i++;
		}
	}
	
	private void run()
	{
		Configs configs = null;
		JST.Program program = null;
		
		try {
			configs = Configs.loadConfigFile(_configsFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (configs != null)
			program = Generator.generate(configs, false);
		
		if (program != null) {
			String programStr = JstToJs.executeCostum(program,"  ","\n");
			
			if (_outputFile != null)
			{
				try {
					Utils.FilesIO.WriteToFile(_outputFile, programStr);
					
					System.out.println("new random program was saved to " + _outputFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				System.out.println(programStr);
		}
	}
}