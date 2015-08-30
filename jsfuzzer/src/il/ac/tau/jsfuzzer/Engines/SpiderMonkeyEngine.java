package il.ac.tau.jsfuzzer.Engines;

import java.io.File;

public class SpiderMonkeyEngine extends AbstractEngine
{
	public SpiderMonkeyEngine(File runEnginesFolder)
	{
		super("SpiderMonkey", "js.exe", runEnginesFolder);
	}
}