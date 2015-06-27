package Engines;

public class SpiderMonkeyEngine extends AbstractEngine
{	
	public SpiderMonkeyEngine()
	{
		super("SpiderMonkey");
	}
	
	@Override
	protected String[] getCommandLineList(String filePath)
	{
		String[] list = {"../JSEngines/SpiderMonkey/js.exe",filePath};
		
		return list;
	}
}