package il.ac.tau.jsfuzzer.Engines;

import java.io.File;

public class DynjsEngine extends AbstractEngine
{
	public DynjsEngine(File runEnginesFolder)
	{
		super("Dynjs", "dynjs.jar", runEnginesFolder);
	}

	@Override
	protected String[] getCommandLineList(File filePath)
	{
		return new String[] {"java", "-jar", _execFile.getPath(), filePath.toString()};
	}
}