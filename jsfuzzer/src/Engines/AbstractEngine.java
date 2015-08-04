package Engines;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import Utils.FilesIO;
import Utils.TimerRunner;

public abstract class AbstractEngine
{
	protected static String _enginesFolder = FilesIO.getBinDirGlobalLocation() + "../JSEngines/";
	private String _platformName;
	
	public AbstractEngine(String name)
	{
		_platformName = name;
	}
	
	public String getPlatformName()
	{
		return _platformName;
	}
	
	public RunEngineResult runFile(File filePath, final int timeoutMilliseconds)
	{
		
		RunEngineResult runResult = null;
		
		try
		{
			// create new js process
			final ProcessBuilder pb = new ProcessBuilder(getCommandLineList(filePath));
			
			// redirect process stdout and stdin to temporary files
			File outputFile = (Files.createTempFile(filePath.getName(), ".out.txt")).toFile();
			File errorFile = (Files.createTempFile(filePath.getName(), ".err.txt")).toFile();
			pb.redirectOutput(outputFile);
			pb.redirectError(errorFile);
			
			// execute js process with timeout limitation
			// if timeout limitation was exceeded - kill the process
			// also measure running time
			TimerRunner<Boolean> worker = new TimerRunner<Boolean>()
			{
				@Override public Boolean run()
				{
					boolean timeoutExcided = false;
					
					try {
						Process p = pb.start();
						if (!p.waitFor(timeoutMilliseconds, TimeUnit.MILLISECONDS))
						{
							timeoutExcided = true;
							p.destroy();
						}
					} catch (IOException | InterruptedException e) { e.printStackTrace(); }
	
					return timeoutExcided;
				}
			};
			
			// read stdout and stderr for files
			String stdout = FilesIO.ReadFile(outputFile.getAbsolutePath());
			String stderr = FilesIO.ReadFile(errorFile.getAbsolutePath());
			
			// delete temporary files
			outputFile.delete();
			outputFile.delete();		
			
			// compose run engine result
			runResult = new RunEngineResult(stdout, stderr, worker.lastResult(), worker.lastRuntime());
		}
		catch (IOException e) { e.printStackTrace(); }
		
		return runResult;
	}
	
	/** this method should return a array of cli command to be executed */
	protected abstract String[] getCommandLineList(File filePath);
}