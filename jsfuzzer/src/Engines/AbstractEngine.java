package Engines;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
		
		// redirect stdout to file
		String outFile = generateOutFileName(filePath);
		procBuild.redirectOutput(new File(outFile));
		
		RunEngineResult runResult = new RunEngineResult();
		
		// execute program
		try
		{
			Process proc = procBuild.start();
			proc.waitFor();

			// read output
			String stdout = Utils.FilesIO.ReadFile(outFile);
			String stderr = readProcessError(proc);
			
			runResult.setResult(stdout, stderr);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return runResult;
	}
	
	/** this method should return a array of cli command to be executed */
	protected abstract String[] getCommandLineList(String filePath);
	
	private String readProcessError(Process p)
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
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
	
	private String generateOutFileName(String file)
	{
		return String.format("%s.%s.out", file, _platformName);
	}
}