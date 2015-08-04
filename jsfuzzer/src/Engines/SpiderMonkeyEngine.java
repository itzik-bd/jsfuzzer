package Engines;

import java.io.File;

public class SpiderMonkeyEngine extends AbstractEngine
{	
	public SpiderMonkeyEngine()
	{
		super("SpiderMonkey");
	}
	
	@Override
	protected String[] getCommandLineList(File filePath)
	{
		String[] list = {_enginesFolder + "SpiderMonkey/js.exe",filePath.toString()};
		
		return list;
	}
}