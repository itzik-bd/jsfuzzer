package Engines;

public class NodejsEngine extends AbstractEngine
{
	public NodejsEngine()
	{
		super("NodeJs");
	}
	
	@Override
	protected String[] getCommandLineList(String filePath)
	{
		String[] list = {"../JSEngines/Nodejs/node.exe", filePath};
		
		return list;
	}
}