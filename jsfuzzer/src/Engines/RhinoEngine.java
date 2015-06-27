package Engines;

public class RhinoEngine extends AbstractEngine
{
	public RhinoEngine()
	{
		super("rhino");
	}
	
	@Override
	protected String[] getCommandLineList(String filePath)
	{
		String[] list = {"java", "-jar", "../JSEngines/Rhino/js.jar", filePath};
		
		return list;
	}
}