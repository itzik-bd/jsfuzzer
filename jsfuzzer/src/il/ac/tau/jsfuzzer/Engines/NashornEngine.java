package il.ac.tau.jsfuzzer.Engines;

import java.io.File;

public class NashornEngine extends AbstractEngine
{
	public NashornEngine(File runEnginesFolder)
	{
		super("Nashorn", "jjs.exe", runEnginesFolder);
	}
}