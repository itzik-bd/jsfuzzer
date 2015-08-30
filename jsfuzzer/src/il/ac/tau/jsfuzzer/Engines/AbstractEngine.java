package il.ac.tau.jsfuzzer.Engines;

import il.ac.tau.jsfuzzer.Utils.FilesIO;
import il.ac.tau.jsfuzzer.Utils.OutLog;
import il.ac.tau.jsfuzzer.Utils.TimerRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public abstract class AbstractEngine
{
	private final String _engineName;
	private final File _engineFolder; // = FilesIO.PROJECT_PATH + "resources/engines/";
	
	protected final File _execFile;
	
	public AbstractEngine(String name, String execFile, File runEnginesFolder)
	{
		_engineName = name;
		_engineFolder = new File(runEnginesFolder, name);
		_execFile = new File(_engineFolder, execFile);
	}
	
	public String getEngineName()
	{
		return _engineName;
	}
	
	/**
	 * this method should return a array of cli command to be executed.
	 * current implementation returns a new process cli.
	 * override to return a new process cli (running java for example) 
	 */
	protected String[] getCommandLineList(File filePath)
	{
		return new String[] {_execFile.getPath(), filePath.toString()};
	}
	
	/** this method check if the engine folder exists and all files are ok */
	public final boolean validateEngineDirectoryAndFiles()
	{
		// check that current engine has a directory
		if (!_engineFolder.isDirectory())
		{
			OutLog.printError(String.format("Could not find %s's engine folder (%s)", _engineName, _engineFolder));
			return false;
		}
		
		// check that the executable exists
		if (!_execFile.exists()) {
			OutLog.printError(String.format("%s: executable '%s' could not be found in engine's directory", _engineName, _execFile.getName()));
			return false;
		}
		
		// return true if validate file was OK
		return true;
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
}