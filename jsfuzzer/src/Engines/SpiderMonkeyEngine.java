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
		String[] list = {"../JSEngines/SpiderMonkey/js.exe",filePath.toString()};
		
		return list;
	}
}