package il.ac.tau.jsfuzzer.Engines;

import java.io.File;

public class RhinoEngine extends AbstractEngine
{
	public RhinoEngine(File runEnginesFolder)
	{
		super("Rhino", "js.jar", runEnginesFolder);
	}
	
	@Override
	protected String[] getCommandLineList(File filePath)
	{
		return new String[] {"java", "-jar", _execFile.getPath(), filePath.toString()};
	}
}