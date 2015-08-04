package Engines;

import java.io.File;

public class NodejsEngine extends AbstractEngine
{
	public NodejsEngine()
	{
		super("NodeJs");
	}
	
	@Override
	protected String[] getCommandLineList(File filePath)
	{
		String[] list = {_enginesFolder + "Nodejs/node.exe", filePath.toString()};
		
		return list;
	}
}