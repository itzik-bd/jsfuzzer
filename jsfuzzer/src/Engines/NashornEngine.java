package Engines;

public class NashornEngine extends AbstractEngine
{
	public NashornEngine()
	{
		super("Nashorn");
	}
	
	@Override
	protected String[] getCommandLineList(String filePath)
	{
		String[] list = {"../JSEngines/Nashorn/jjs.exe", filePath};
		
		return list;
	}
}