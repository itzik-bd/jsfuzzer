package Engines;

import java.io.File;

public class NashornEngine extends AbstractEngine
{
	public NashornEngine()
	{
		super("Nashorn");
	}
	
	@Override
	protected String[] getCommandLineList(File filePath)
	{
		String[] list = {_enginesFolder + "Nashorn/jjs.exe", filePath.toString()};
		
		return list;
	}
}