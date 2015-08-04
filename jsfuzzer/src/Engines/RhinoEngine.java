package Engines;

import java.io.File;

public class RhinoEngine extends AbstractEngine
{
	public RhinoEngine()
	{
		super("rhino");
	}
	
	@Override
	protected String[] getCommandLineList(File filePath)
	{
		String[] list = {"java", "-jar", _enginesFolder + "Rhino/js.jar", filePath.toString()};
		
		return list;
	}
}