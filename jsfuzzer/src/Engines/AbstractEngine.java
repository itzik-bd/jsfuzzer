package Engines;

import java.io.File;
import java.io.IOException;

public abstract class AbstractEngine
{
	private String _platformName;
	private String _platformExe;
	
	public AbstractEngine(String name, String exe)
	{
		_platformName = name;
		_platformExe = exe;
	}
	
	public void runFile(String filePath)
	{
		ProcessBuilder proc = new ProcessBuilder(_platformExe, filePath);
		File outputFile = new File(generateFileName(filePath));
		proc.redirectOutput(outputFile);
		try {
			proc.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected String generateFileName(String file)
	{
		return String.format("%s.%s.out", file, _platformName);
	}
}

