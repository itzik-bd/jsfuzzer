package il.ac.tau.jsfuzzer.Engines;

import java.io.File;

public class NodejsEngine extends AbstractEngine
{	
	public NodejsEngine(File runEnginesFolder)
	{
		super("NodeJs", (IS_WINDOWS ? "node.exe" : "node"), runEnginesFolder);
	}
}