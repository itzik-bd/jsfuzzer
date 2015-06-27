package Engines;

public class DynjsEngine extends AbstractEngine
{
	public DynjsEngine()
	{
		super("dynjs");
	}

	@Override
	protected String[] getCommandLineList(String filePath)
	{
		String[] list = {"java", "-jar", "../JSEngines/Dynjs/dynjs.jar", filePath};
		
		return list;
	}

}