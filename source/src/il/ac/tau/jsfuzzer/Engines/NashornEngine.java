package il.ac.tau.jsfuzzer.Engines;

import java.io.File;

public class NashornEngine extends AbstractEngine
{
	public NashornEngine(File runEnginesFolder)
	{
		super("Nashorn", "launcher.jar", runEnginesFolder);
	}
	
	@Override
	protected String[] getCommandLineList(File filePath)
	{
		return new String[] {"java", "-jar", _execFile.getPath(), filePath.toString()};
	}
}