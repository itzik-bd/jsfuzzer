package Engines;

import java.io.File;
import java.io.IOException;

public class NodejsEngine extends AbstractEngine
{
	private static String nodejsPath = "C:/Program Files/nodejs/node.exe";
	public static int fileCount = 0;
	public String dirPath = "testsOutput/NodejsTests";
	
	@Override
	public void runFile(String filePath)
	{
		fileCount++;
		ProcessBuilder proc = new ProcessBuilder(nodejsPath, filePath);
		File outputFile = new File(String.format("%s/nodejs_test_%d.txt", dirPath, fileCount));
		proc.redirectOutput(outputFile);
		try {
			proc.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
