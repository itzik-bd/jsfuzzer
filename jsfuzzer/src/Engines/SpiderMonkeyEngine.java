package Engines;

import java.io.File;
import java.io.IOException;

public class SpiderMonkeyEngine extends AbstractEngine
{
	private static String spiderMonkeyPath = "../JSEngines/SpiderMonkey/js.exe";
	public static int fileCount = 0;
	public String dirPath = "testsOutput/SpiderMonkeyTests";
	
	@Override
	public void runFile(String filePath)
	{
		fileCount++;
		ProcessBuilder proc = new ProcessBuilder(spiderMonkeyPath, filePath);
		File outputFile = new File(String.format("%s/spider_monkey_test_%d.txt", dirPath, fileCount));
		proc.redirectOutput(outputFile);
		try {
			proc.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
