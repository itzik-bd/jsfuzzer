package Engines;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class AbstractEngine
{
	private String _platformName;
	
	public AbstractEngine(String name)
	{
		_platformName = name;
	}
	
	public String getPlatformName()
	{
		return _platformName;
	}
	
	public RunEngineResult runFile(String filePath)
	{		
		// create new js process
		ProcessBuilder procBuild = new ProcessBuilder(getCommandLineList(filePath));		
		RunEngineResult runResult = null;
		
		// execute program
		try
		{
			// run process
			Process proc = procBuild.start();
			
			// read proccess stdout and stdin
			String stdout = communicateProcess(proc.getInputStream()); // getInputStream actually returns the stdout!
			String stderr = communicateProcess(proc.getErrorStream());
			
			runResult = new RunEngineResult(stdout, stderr);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return runResult;
	}
	
	/** this method should return a array of cli command to be executed */
	protected abstract String[] getCommandLineList(String filePath);
	
	private String communicateProcess(InputStream is)
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String lineFeed = System.getProperty("line.separator");
		StringBuffer sb = new StringBuffer();
		String line;
		try {
			while ((line = in.readLine()) != null) {
			    sb.append(line+lineFeed);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}
}