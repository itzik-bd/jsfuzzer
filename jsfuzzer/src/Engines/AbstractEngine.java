package Engines;

import java.io.File;
import java.io.IOException;

public abstract class AbstractEngine
{
	private String _platformName;
	
	public AbstractEngine(String name)
	{
		_platformName = name;
	}
	
	public void runFile(String filePath)
	{
		System.out.println(String.format("Running file '%s' over engine '%s'", filePath, _platformName));
		
		// create new js process
		ProcessBuilder proc = new ProcessBuilder(getCommandLineList(filePath));
		
		// redirect stdout and stderr
		proc.redirectOutput(new File(generateFileName(filePath)));
		proc.redirectError(new File(generateErrFileName(filePath)));
		
		// execute program
		try { proc.start(); }
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** this method should return a array of cli command to be executed */
	protected abstract String[] getCommandLineList(String filePath);
	
	protected String generateFileName(String file)
	{
		return String.format("%s.%s.out", file, _platformName);
	}
	
	protected String generateErrFileName(String file)
	{
		return String.format("%s.%s.err", file, _platformName);
	}
}

