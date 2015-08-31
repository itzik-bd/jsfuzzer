package il.ac.tau.jsfuzzer.Engines;

import java.io.File;

public class SpiderMonkeyEngine extends AbstractEngine
{
	// no support for linux
	public SpiderMonkeyEngine(File runEnginesFolder)
	{
		super("SpiderMonkey", (IS_WINDOWS ? "js.exe" : null), runEnginesFolder);
	}
}