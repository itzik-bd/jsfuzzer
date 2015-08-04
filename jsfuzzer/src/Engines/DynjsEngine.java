package Engines;

import java.io.File;

public class DynjsEngine extends AbstractEngine
{
	public DynjsEngine()
	{
		super("dynjs");
	}

	@Override
	protected String[] getCommandLineList(File filePath)
	{
		String[] list = {"java", "-jar", _enginesFolder + "Dynjs/dynjs.jar", filePath.toString()};
		
		return list;
	}

}